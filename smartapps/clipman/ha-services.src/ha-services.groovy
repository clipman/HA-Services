/**
 *  HA-Services v2022-04-15
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
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", nextPage: null, uninstall: true, install: true) {
		section("Configure Home Assistant API") {
		   input "haURL", "text", title: "HomeAssistant external URL(ex, https://xxx.duckdns.org)", required: true
		   input "haToken", "text", title: "HomeAssistant Token", required: true
		}
		section("[HA -> ST]") {
		   href "haDevicePage", title: "Get HA Devices", description:""
		   input "haDevice", "text", title: "Filter", required: false
		   href "haAddDevicePage", title: "Add HA Device", description:""
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
            if(settings.haDevice == null || settings.haDevice == "") {
				if(entity_id.contains("light.") || entity_id.contains("switch.") || entity_id.contains("fan.") || entity_id.contains("cover.") || entity_id.contains("lock.") || entity_id.contains("vacuum.") || entity_id.contains("button.") || entity_id.contains("climate.") || entity_id.contains("media_player.") || entity_id.contains("input_boolean.") || entity_id.contains("input_button.") || entity_id.contains("script.") || entity_id.contains("rest_command.")) {
					if(!entity_id.contains("_st")) {
						list.push("${friendly_name} [ ${entity_id} ]")
					}
				}
			} else {
				if(entity_id.contains(settings.haDevice) || friendly_name.contains(settings.haDevice)) {
					if(!entity_id.contains("_st")) {
						list.push("${friendly_name} [ ${entity_id} ]")
					}
				}
			}
		}
	}
	dynamicPage(name: "haAddDevicePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] Add HA Devices") {
			input(name: "selectedAddHADevice", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
	}

}

def installed() {
	initialize()
	if (!state.accessToken) {
		createAccessToken()
	}
	app.updateSetting("selectedAddHADevice", "None")
}

def updated() {
	log.info "Updated with settings: ${settings}"
	initialize()
	app.updateSetting("selectedAddHADevice", "None")
}

def initialize() {
	addHAChildDevice()
}

def addHAChildDevice() {
	if(settings.selectedAddHADevice) {
		if(settings.selectedAddHADevice != "None") {
			//log.debug "ADD >> " + settings.selectedAddHADevice
			def tmp = settings.selectedAddHADevice.split(" \\[ ")
			def tmp2 = tmp[1].split(" \\]")
			def entity_id = tmp2[0]
			def dni = entity_id
			def haDevice = getHADeviceByEntityId(entity_id)
			if(haDevice) {
				def dth = "HomeAssistant Services"
				def name = haDevice.attributes.friendly_name
				if(!name) {
					name = entity_id
				}
				try {
					//def childDevice = addChildDevice("clipman", dth, dni, location.hubs[0].id, ["label": name])
					def childDevice = addChildDevice("clipman", dth, dni, "", ["label": name])
					childDevice.setHASetting(settings.haURL, settings.haToken, entity_id)
					childDevice.setStatus(haDevice.state)
					childDevice.refresh()
				} catch(err) {
					log.error "Add HA Device ERROR >> ${err}"
				}
			}
		}
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

def getDataList() {
	def options = [
		"method": "GET",
		"path": "/api/states",
		"headers": [
			"HOST": settings.haURL,
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
		msg.json.each {
            def entity_type = it.entity_id.split('\\.')[0]
            if(entity_type != "sensor" && entity_type != "binary_sensor") {
                //HA의 Entity Data가 많으면 에러가 발생하기 때문에 Data량을 최대한 줄임
                def obj = [entity_id: "${it.entity_id}", attributes: [friendly_name: "${it.attributes.friendly_name}"]]
                //def obj = [entity_id: "${it.entity_id}", attributes: [friendly_name: ""]]
			    json.push(obj)
            }
		}
		state.dataList = json
		state.latestHttpResponse = status
	} catch (e) {
		log.warn "Exception caught while parsing data: "+e
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
			//log.debug "HA->ST >> [${dni}] state:${params.value}  attr:${attr}  oldstate:${oldstate}" + ((params?.unit) ? "  unit:${params.unit}" : "")
            if (params.value != oldstate) {
                device.setStatus(params.value)
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
		haDevices.push(childDevice.deviceNetworkId.substring(13))
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
				access_token:  state.accessToken
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
		path("/get")    { action: [POST: "authError"] }
	} else {
		path("/config")	{ action: [GET: "renderConfig"] }
		path("/update")	{ action: [GET: "updateDevice"] }
        path("/getHADevices") { action: [GET: "getHADevices"] }
		path("/get")    { action: [POST: "updateSTDevice"] }
	}
}
