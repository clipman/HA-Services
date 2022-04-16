/**
 *  HA-Services v2022-04-17
 *  clipman@naver.com
 *  날자
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.transform.Field

definition(
	name: "HA-Services",
	namespace: "clipman",
	author: "clipman",
	description: "Home Assistant Services Call",
	category: "My Apps",
	iconUrl: "https://github.com/home-assistant/assets/blob/master/logo/logo-small.png?raw=true",
	iconX2Url: "https://github.com/home-assistant/assets/blob/master/logo/logo-small.png?raw=true",
	iconX3Url: "https://github.com/home-assistant/assets/blob/master/logo/logo-small.png?raw=true",
	oauth: true
)

preferences {
	page(name: "mainPage")
	page(name: "haDevicePage")
	page(name: "haAddDevicePage")
	page(name: "haDeleteDevicePage")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", nextPage: null, uninstall: true, install: true) {
		section("Configure Home Assistant API") {
			paragraph "Home Assistant 외부접속 URL, https://xxx.duckdns.org 또는 http://xxx.duckdns.org:8123"
			input "haURL", "text", title: "HomeAssistant external URL", required: true
			input "haToken", "text", title: "HomeAssistant Token", required: true
		}
		section("[HA -> ST]") {
			href "haDevicePage", title: "Get HA Devices", description:""
			input "haDeviceFilter", "text", title: "Filter", required: false
			href "haAddDevicePage", title: "Add HA Device", description:""
		}
		section("[HA -> ST] Delete") {
			href "haDeleteDevicePage", title: "Delete HA Device", description:""
		}
		section() {
			paragraph "View this SmartApp's configuration to use it in other places."
			href url:"${apiServerUrl("/api/smartapps/installations/${app.id}/config?access_token=${state.accessToken}")}", style:"embedded", required:false, title:"Config", description:"Tap, select, copy, then click \"Done\""
			label title: "App Label (optional)", description: "Rename this App", defaultValue: app?.name, required: false
		}
	}
}

def haDevicePage() {
	//log.debug "Executing haDevicePage"
	getDataList()

	dynamicPage(name: "haDevicePage", title:"[HA -> ST] Get HA Devices", refreshInterval:5) {
		section("Please wait for the API to answer, this might take a couple of seconds.") {
			if(state.latestHttpResponse) {
				if(state.latestHttpResponse == 200) {
					paragraph "Connected \nOK: 200"
				} else {
					paragraph "Connection error \nHTTP response code: " + state.latestHttpResponse
				}
			}
		}
	}
}

def haAddDevicePage() {
	def addedDNIList = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		addedDNIList.push(childDevice.deviceNetworkId)
	}

	def list = []
	list.push("None")
	state.dataList.each {
		def entity_id = "${it.entity_id}"
		def friendly_name = "${it.attributes.friendly_name}"
		if(friendly_name == null) {
			friendly_name = ""
		}
		if(!addedDNIList.contains(entity_id)) {
			if(!settings.haDeviceFilter) {
				if(!entity_id.contains("_st")) {
					list.push("${entity_id} [${friendly_name}]")
				}
			} else {
				if(entity_id.contains(settings.haDeviceFilter) || friendly_name.contains(settings.haDeviceFilter)) {
					if(!entity_id.contains("_st")) {
						list.push("${entity_id} [${friendly_name}]")
					}
				}
			}
		}
	}
	list.sort()
	dynamicPage(name: "haAddDevicePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] Add HA Device") {
			input(name: "selectedAddHADevice", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
		section ("Device Name") {
			input(name: "haAddName", title: "Name (optional)", type: "text", required: false, description: "Rename selected device", value: "")
		}
	}
}

def haDeleteDevicePage() {
	def list = []
	list.push("None")
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		list.push(childDevice.deviceNetworkId + " [" + childDevice.label + "]")
	}
	dynamicPage(name: "haDeleteDevicePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] Delete HA Device") {
			input(name: "selectedDeleteHADevice", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
	}
}

def installed() {
	initialize()
	if (!state.accessToken) {
		createAccessToken()
	}
	app.updateSetting("selectedAddHADevice", "None")
	app.updateSetting("selectedDeleteHADevice", "None")
	app.updateSetting("haAddName", "")
}

def updated() {
	log.info "Updated with settings: ${settings}"
	initialize()
	app.updateSetting("selectedAddHADevice", "None")
	app.updateSetting("selectedDeleteHADevice", "None")
	app.updateSetting("haAddName", "")
}

def initialize() {
	deleteChildDevice()
	addHAChildDevice()
	refreshRegisteredHADeviceList()
}

def deleteChildDevice() {
	if(settings.selectedDeleteHADevice) {
		if(settings.selectedDeleteHADevice != "None") {
			//log.debug "DELETE >> " + settings.selectedDeleteHADevice
			def nameAndDni = settings.selectedDeleteHADevice.split(" \\[")
			try {
				deleteChildDevice(nameAndDni[0])
			} catch(err) {
				//
			}
		}
	}
}

def addHAChildDevice() {
	if(settings.selectedAddHADevice) {
		if(settings.selectedAddHADevice != "None") {
			//log.debug "ADD >> " + settings.selectedAddHADevice
			//list.push("${entity_id} [${friendly_name}]")
			def tmp = settings.selectedAddHADevice.split(" \\[")
			def entity_id = tmp[0]
			def dni = entity_id
			def haDevice = getHADeviceByEntityId(entity_id)
			if(haDevice) {
				def dth = "HomeAssistant Services"
				def name = haAddName
				if(!name) {
					name = haDevice.attributes.friendly_name
					if(!name) {
						name = entity_id
					}
				}
				try {
					//def childDevice = addChildDevice("clipman", dth, dni, location.hubs[0].id, ["label": name])
					def childDevice = addChildDevice("clipman", dth, dni, "", ["label": name])
					childDevice.setStatus(haDevice.state)
				} catch(err) {
					log.error "Add HA Device ERROR >> ${err}"
				}
			}
		}
	}
}

/*
def refreshRegisteredHADeviceList() {
	def options = [
		"method": "POST",
		"path": "/api/services/ha_connector/refresh",
		"headers": [
			"HOST": settings.haAddress,
			"Authorization": "Bearer ${settings.haPassword}",
			"Content-Type": "application/json"
		],
		"body": []
	]

	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: null])
	sendHubCommand(myhubAction)
}
*/
def refreshRegisteredHADeviceList() {
	def service = "/api/services/ha_services/refresh"
	def params = [
		uri: haURL,
		path: service,
		headers: ["Authorization": "Bearer " + haToken],
		requestContentType: "application/json"
	]
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "HomeAssistant Services Error: $e"
		return false
	}
}

def getHADeviceByEntityId(entity_id) {
	def target
	state.dataList.each { haDevice ->
		if(haDevice.entity_id == entity_id) {
			target = haDevice
		}
	}
	target
}

/*
def getDataList() {
	def options = [
		"method": "GET",
		"path": "/api/states",
		"headers": [
			"HOST": "192.168.219.130:8123",
			"Authorization": "Bearer ${settings.haToken}",
			"Content-Type": "application/json"
		]
	]

	def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: dataCallback])
	sendHubCommand(myhubAction)
}

def dataCallback(physicalgraph.device.HubResponse hubResponse) {
	def msg, status, json = []
	try {
		msg = parseLanMessage(hubResponse.description)
		status = msg.status
		log.info "msg: ${msg}"
		msg.json.each {
			def entity_type = it.entity_id.split('\\.')[0]
			if(entity_type != "sensor" && entity_type != "binary_sensor") {
				//HA의 Entity Data가 많으면 에러가 발생하기 때문에 Data량을 최대한 줄임
				//def obj = [entity_id: "${it.entity_id}", attributes: [friendly_name: "${it.attributes.friendly_name}"]]
				def obj = [entity_id: "${it.entity_id}", state: "${it.state}", attributes: [friendly_name: "${it.attributes.friendly_name}"]]
				json.push(obj)
			}
		}
		state.dataList = json
		state.latestHttpResponse = status
	} catch (e) {
		log.warn "Exception caught while parsing data: "+e
	}
}
*/

def getDataList() {
	def service = "/api/states"
	def params = [
		uri: haURL,
		path: service,
		headers: ["Authorization": "Bearer " + haToken],
		requestContentType: "application/json"
	]
	def switchEntity = ["switch", "light", "climate", "fan", "vacuum", "cover", "lock", "script", "rest_command", "esphome",
						"button", "input_button", "automation", "camera", "input_boolean", "media_player"]
	def json = []
	try {
		httpGet(params) { resp ->
			resp.headers.each {
				//log.debug "${it.name} : ${it.value}"
			}
			if (resp.status == 200) {
				//log.debug "resp.data: ${resp.data}"
				resp.data.each {
					def entity_type = it.entity_id.split('\\.')[0]
					if(switchEntity.contains(entity_type)) {
						def obj = [entity_id: "${it.entity_id}", state: "${it.state}", attributes: [friendly_name: "${it.attributes.friendly_name}"]]
						json.push(obj)
					}
				}
				state.dataList = json
			}
			state.latestHttpResponse = resp.status
		}
	} catch (e) {
		state.latestHttpResponse = 401
		log.error "HomeAssistant Services Error: $e"
	}
}

//HA->ST
def updateDevice() {
	def dni = params.entity_id
	def attr = null
	def oldstate = null
	try {
		attr = new groovy.json.JsonSlurper().parseText(new String(params.attr.decodeBase64()))
	} catch(err) {
		//log.debug "${dni} attr decoding error : "+params.attr
	}
	oldstate = params?.old
	try {
		def device = getChildDevice(dni)
		if(device) {
			log.debug "HA->ST >> [${dni}] state:${params.value}  attr:${attr}  oldstate:${oldstate}" + ((params?.unit) ? "  unit:${params.unit}" : "")
			//HA->ST >> [climate.kocom_room2_thermostat] state:heat attr:[current_temperature:24.0, friendly_name:작은방난방, hvac_modes:[off, heat], max_temp:25.0, min_temp:20.0, supported_features:1, target_temp_step:1.0, temperature:23.0] oldstate:off
			if (params.value != oldstate) {
				def entity_type = dni.split('\\.')[0]
				def onOff = params.value
				switch(entity_type) {
					case "vacuum":
						if(params.value == "docked" || params.value == "returning") {
							onOff = "off"
						} else {
							onOff = "on"
						}
						break;
					case "cover":
						if(params.value == "open" || params.value == "opening" || params.value == "partially open") {
							onOff = "on"
						} else {
							onOff = "off"
						}
						break;
					case "lock":
						if(params.value == "locked") {
							onOff = "off"
						} else {
							onOff = "on"
						}
						break;
					case "climate":
						if(params.value == "off") {
							onOff = "off"
						} else {
							onOff = "on"
						}
						break;
					default:	//switch, light, fan, input_boolean, ...
						break;
				}
				device.setStatus(onOff)	// 상태만 반영함(sendEvent())
			}
		}
	} catch(err) {
		log.error "${err}"
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([result: true])
	render contentType: "application/json", data: deviceJson
}

//HA->ST HA에서 변경한 Switch상태를 ST에 반영(on, off)
def updateSTDevice() {
	//log.debug "POST >>>> params:${params}"
	//params:[turn:on, attributes:switch, dni:anbang_gonggiceongjeonggi, ...]
	def state = params.turn					//"${params.turn}"	// on, off

	if(settings["switch"]) {				//switch
		settings["switch"].each { device ->
			if(device.name == params.dni) {
				device."$params.turn"()
			}
		}
	}
	render contentType: "text/html", data: state
}

def getHADevices() {
	def haDevices = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		haDevices.push(childDevice.deviceNetworkId)
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([list: haDevices])
	render contentType: "application/json", data: deviceJson
}

def authError() {
	[error: "Permission denied"]
}

def renderConfig() {
	def configJson = new groovy.json.JsonOutput().toJson([
		description: "HA-Services API",
		platforms: [
			[
				platform: "SmartThings HA-Services",
				name: "HA-Services",
				app_url: apiServerUrl("/api/smartapps/installations/"),
				app_id: app.id,
				access_token: state.accessToken
			]
		],
	])
	def configString = new groovy.json.JsonOutput().prettyPrint(configJson)
	render contentType: "text/plain", data: configString
}

mappings {
	if (!params.access_token || (params.access_token && params.access_token != state.accessToken)) {
		path("/config")	{ action: [GET: "authError"] }
		path("/update")	{ action: [GET: "authError"] }
		path("/getHADevices") { action: [GET: "authError"] }
		path("/get")	{ action: [POST: "authError"] }
	} else {
		path("/config")	{ action: [GET: "renderConfig"] }
		path("/update")	{ action: [GET: "updateDevice"] }
		path("/getHADevices") { action: [GET: "getHADevices"] }
		path("/get")	{ action: [POST: "updateSTDevice"] }
	}
}
