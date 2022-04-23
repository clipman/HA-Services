/**
 *  HA-Services v2022-04-21
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

@Field
deviceEntity = ["switch", "light", "climate", "fan", "vacuum", "cover", "lock", "script", "rest_command",
				"button", "input_button", "automation", "camera", "input_boolean", "media_player"]
@Field
sensorEntity = ["sensor", "binary_sensor", "device_tracker", "input_datetime", "input_number", "input_text", "zone"]

definition(
	name: "HA-Services",
	namespace: "clipman",
	author: "clipman",
	description: "HomeAssistant의 장치/센서들을 Smartthing로 가져오고 서비스를 호출합니다.",
	category: "My Apps",
	iconUrl: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX2Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX3Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	oauth: true
)

preferences {
	page(name: "mainPage")
	page(name: "entityPage")
	page(name: "devicePage")
	page(name: "sensorPage")
	page(name: "deletePage")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "", nextPage: null, uninstall: true, install: true) {
		section("HomeAssistant 연결 설정") {
			paragraph "https://xxx.duckdns.org 또는 http://xxx.duckdns.org:8123"
			input "haURL", "text", title: "HomeAssistant URL", required: true
			input "haToken", "text", title: "HomeAssistant Token", required: true
		}
		section("[HA -> ST] 장치/센서 가져오기") {
			input "entityFilter", "text", title: "추가할 장치/센서를 선택하기 쉽게 필터처리 (선택사항)", required: false
			href "entityPage", title: "추가 가능한 장치/센서들을 읽어오기(1)"
			href "devicePage", title: "추가할 장치 선택(2)"
			href "sensorPage", title: "추가할 센서 선택(3)"
		}
		section("[HA -> ST] 장치/센서 삭제") {
			href "deletePage", title: "삭제할 장치/센서 선택(4)"
		}
		section() {
			href url:"${apiServerUrl("/api/smartapps/installations/${app.id}/config?access_token=${state.accessToken}")}", style:"embedded", required:false, title:"HomeAssistant 설정 정보 읽어오기(0)", description:"누르고 선택하고 복사한 후에 \"완료\"를 누르세요."
		}
		section() {
			label title: "앱의 이름 변경 (선택사항)", description: "앱의 이름을 바꿀 수 있어요", defaultValue: app?.name, required: false
		}
	}
}

def entityPage() {
	getEntityList()
	dynamicPage(name: "entityPage", title:"[HA -> ST] 장치/센서 가져오기", refreshInterval:5) {
		section("추가 가능한 HA 장치/센서들을 읽어옵니다.") {
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

def devicePage() {
	def addedDNIList = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		addedDNIList.push(childDevice.deviceNetworkId)
	}

	def list = []
	list.push("None")
	state.dataDeviceList.each {
		def entity_id = "${it.id}"			//def entity_id = "${it.entity_id}"
		def friendly_name = "${it.name}"	//def friendly_name = "${it.friendly_name}"
		if(friendly_name == null) {
			friendly_name = ""
		}
		if(!existEntityInList(addedDNIList, entity_id)) {
			if(!settings.entityFilter) {
				list.push("${entity_id} [${friendly_name}]")
			} else {
				if(entity_id.contains(settings.entityFilter) || friendly_name.contains(settings.entityFilter)) {
					list.push("${entity_id} [${friendly_name}]")
				}
			}
		}
	}
	list.sort()
	dynamicPage(name: "devicePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] 장치 가져오기") {
			input(name: "selectedAddDevice", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
		section ("장치 이름") {
			input(name: "addDeviceName", title: "이름 (선택사항)", type: "text", required: false, description: "선택한 장치의 이름을 바꿀 수 있어요", value: "")
		}
	}
}

def sensorPage() {
	def addedDNIList = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		addedDNIList.push(childDevice.deviceNetworkId)
	}

	def list = []
	list.push("None")
	state.dataSensorList.each {
		def entity_id = "${it.id}"			//def entity_id = "${it.entity_id}"
		def friendly_name = "${it.name}"	//def friendly_name = "${it.friendly_name}"
		if(friendly_name == null) {
			friendly_name = ""
		}
		if(!existEntityInList(addedDNIList, entity_id)) {
			if(!settings.entityFilter) {
				list.push("${entity_id} [${friendly_name}]")
			} else {
				if(entity_id.contains(settings.entityFilter) || friendly_name.contains(settings.entityFilter)) {
					list.push("${entity_id} [${friendly_name}]")
				}
			}
		}
	}
	list.sort()
	dynamicPage(name: "sensorPage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] 센서 가져오기") {
			input(name: "selectedAddSensor", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
		section ("센서 이름") {
			input(name: "addSensorName", title: "이름 (선택사항)", type: "text", required: false, description: "선택한 센서의 이름을 바꿀 수 있어요", value: "")
		}
	}
}

def deletePage() {
	def list = []
	list.push("None")
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		list.push(childDevice.deviceNetworkId + " [" + childDevice.label + "]")
	}
	list.sort()
	dynamicPage(name: "deletePage", nextPage: "mainPage", title:"") {
		section ("[HA -> ST] 장치/센서 삭제") {
			input(name: "selectedDeleteEntity", title:"Select" , type: "enum", required: true, options: list, defaultValue: "None")
		}
	}
}

def installed() {
	initialize()
	if (!state.accessToken) {
		createAccessToken()
	}
	app.updateSetting("selectedAddDevice", "None")
	app.updateSetting("selectedAddSensor", "None")
	app.updateSetting("selectedDeleteEntity", "None")
	app.updateSetting("addDeviceName", "")
	app.updateSetting("addSensorName", "")
}

def updated() {
	log.info "Updated with settings: ${settings}"
	initialize()
	app.updateSetting("selectedAddDevice", "None")
	app.updateSetting("selectedAddSensor", "None")
	app.updateSetting("selectedDeleteEntity", "None")
	app.updateSetting("addDeviceName", "")
	app.updateSetting("addSensorName", "")
}

def initialize() {
	deleteEntity()
	addDevice()
	addSensor()
	refreshEntityList()
}

def deleteEntity() {
	if(settings.selectedDeleteEntity) {
		if(settings.selectedDeleteEntity != "None") {
			def nameAndDni = settings.selectedDeleteEntity.split(" \\[")
			try {
				deleteChildDevice(nameAndDni[0])
			} catch(err) {
			}
		}
	}
}

def addDevice() {
	if(settings.selectedAddDevice) {
		if(settings.selectedAddDevice != "None") {
			def tmp = settings.selectedAddDevice.split(" \\[")
			def entity_id = tmp[0]
			def dni = entity_id
			def haDevice = getEntityById(state.dataDeviceList, entity_id)
			if(haDevice) {
				def dth = "HomeAssistant Devices"
				def label = addDeviceName
				if(!label) {
					label = haDevice.name	//label = haDevice.friendly_name
					if(!label) {
						label = entity_id
					}
				}
				def name = entity_id.split("\\.")[1]
				try {
					//def childDevice = addChildDevice("clipman", dth, dni, location.hubs[0].id, ["label": name])
					addChildDevice("clipman", dth, dni, null, ["name": name, "label": label])
				} catch(err) {
					log.error "Add HA Device ERROR >> ${err}"
				}
			}
		}
	}
}

def addSensor() {
	if(settings.selectedAddSensor) {
		if(settings.selectedAddSensor != "None") {
			def tmp = settings.selectedAddSensor.split(" \\[")
			def entity_id = tmp[0]
			def dni = entity_id
			def haSensor = getEntityById(state.dataSensorList, entity_id)
			if(haSensor) {
				def dth = "HomeAssistant Sensors"
				def label = addSensorName
				if(!label) {
					label = haSensor.name	//label = haSensor.friendly_name
					if(!label) {
						label = entity_id
					}
				}
				def name = entity_id.split("\\.")[1]
				try {
					//def childDevice = addChildDevice("clipman", dth, dni, location.hubs[0].id, ["label": name])
					addChildDevice("clipman", dth, dni, null, ["name": name, "label": label])
				} catch(err) {
					log.error "Add HA Sensor ERROR >> ${err}"
				}
			}
		}
	}
}

def refreshEntityList() {
	services("/api/services/ha_services/refresh", [])
}

def services(service, data) {
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json",
		body: data
	]
	//log.info "Services: $params"
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "HomeAssistant Services({$service}) Error: $e"
		return false
	}
}

def getEntityById(list, id) {
	def target
	list.each { item ->
		if(item.id == id) {		//if(item.entity_id == id) {
			target = item
		}
	}
	return target
}

/*
//addedDNIList.contains(entity_id), 이게 잘 안되어서 만들었는데
//이렇게 하면 안됨: 이유는 모름
def existEntityInList(list, id) {
	list.each { item ->
		if(item == id) {
			return true
		}
	}
	return false
}
*/
def existEntityInList(list, id) {
	for (item in list) {
		if(item == id) {
			return true
		}
	}
	return false
}

def getEntityList() {
	def service = "/api/states"
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json"
	]
	def jsonDevice = []
	def jsonSensor = []
	try {
		httpGet(params) { resp ->
			resp.headers.each {
				//log.debug "${it.name} : ${it.value}"
			}
			if (resp.status == 200) {
				//log.debug "resp.data: ${resp.data}"
				resp.data.each {
					def entity_id = it.entity_id
					def friendly_name = it.attributes.friendly_name
					def entity_type = entity_id.split("\\.")[0]
					if(deviceEntity.contains(entity_type)) {
						if(!settings.entityFilter) {
							def objDevice = [id: "${it.entity_id}", name: ""]
							jsonDevice.push(objDevice)
						} else {
							if(entity_id.contains(settings.entityFilter) || friendly_name.contains(settings.entityFilter)) {
								def objDevice = [id: "${it.entity_id}", name: "${it.attributes.friendly_name}"]
								jsonDevice.push(objDevice)
							}
						}
					}
					if(sensorEntity.contains(entity_type)) {
						if(!settings.entityFilter) {
							def objSensor = [id: "${it.entity_id}", name: ""]
							jsonSensor.push(objSensor)
						} else {
							if(entity_id.contains(settings.entityFilter) || friendly_name.contains(settings.entityFilter)) {
								def objSensor = [id: "${it.entity_id}", name: "${it.attributes.friendly_name}"]
								jsonSensor.push(objSensor)
							}
						}
					}
				}
				state.dataDeviceList = jsonDevice
				state.dataSensorList = jsonSensor
			}
			state.latestHttpResponse = resp.status
		}
	} catch (e) {
		state.latestHttpResponse = 401
		log.error "HomeAssistant Services Error: $e"
	}
}

def updateDevice() {
	def dni = params.entity_id
	def attr = null
	def oldstate = null
	try {
		attr = new groovy.json.JsonSlurper().parseText(new String(params.attr.decodeBase64()))
	} catch(err) {
	}
	oldstate = params?.old
	try {
		def device = getChildDevice(dni)
		if(device) {
			log.debug "Device[${dni}] state:${params.value}  attr:${attr}  oldstate:${oldstate}" + ((params?.unit) ? "  unit:${params.unit}" : "")
			if (params.value != oldstate) {
				def state = params.value
				def unit = (params?.unit) ? params.unit : ""
				setStateEntity(device, dni, state, unit, attr)
			} else {
				device.setStatus(params.value, attr)
			}
		}
	} catch(err) {
		log.error "${err}"
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([result: true])
	render contentType: "application/json", data: deviceJson
}

def updateSensor() {
	def dni = params.entity_id
	def attr = null
	def oldstate = null
	try {
		attr = new groovy.json.JsonSlurper().parseText(new String(params.attr.decodeBase64()))
	} catch(err) {
	}
	oldstate = params?.old
	try {
		def device = getChildDevice(dni)
		if(device) {
			log.debug "Sensor[${dni}] state:${params.value}  attr:${attr}  oldstate:${oldstate}" + ((params?.unit) ? "  unit:${params.unit}" : "")
			if (params.value != oldstate) {
				def state = params.value
				def unit = (params?.unit) ? params.unit : ""
				setStateEntity(device, dni, state, unit, attr)
			} else {
				device.setStatus(params.value, attr)
			}
		}
	} catch(err) {
		log.error "${err}"
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([result: true])
	render contentType: "application/json", data: deviceJson
}

def updateEntity(entity_id) {
	def device = getChildDevice(entity_id)
	def service = "/api/states/${entity_id}"
	def params = [
		uri: settings.haURL,
		path: service,
		headers: ["Authorization": "Bearer " + settings.haToken],
		requestContentType: "application/json"
	]
	if(device) {
		try {
			httpGet(params) { resp ->
				if (resp.status == 200) {
					log.debug "updateEntity: ${resp.data}"
					def state = resp.data.state
					def unit = (resp.data.attributes?.unit_of_measurement) ? resp.data.attributes.unit_of_measurement : ""
					setStateEntity(device, entity_id, state, unit, resp.data.attributes)
				}
			}
		} catch (e) {
			log.error "HomeAssistant Services updateEntity Error: $e"
		}
	}
}

def setStateEntity(device, entity_id, value, unit, attributes) {
	def entity_type = entity_id.split("\\.")[0]
	def state = value

	if(entity_type == "vacuum") {
		if(value == "docked" || value == "returning") {
			state = "off"
		} else {
			state = "on"
		}
	} else if(entity_type == "cover") {
		if(value == "open" || value == "opening" || value == "partially open") {
			state = "on"
		} else {
			state = "off"
		}
	} else if(entity_type == "lock") {
		if(value == "locked") {
			state = "off"
		} else {
			state = "on"
		}
	} else if(entity_type == "climate") {
		if(value == "off") {
			state = "off"
		} else {
			state = "on"
		}
	} else if(sensorEntity.contains(entity_type)) {		//} else if(entity_type in sensorEntity) {
		device.setString(value)
		device.setUnit(unit)
		if(unit != "") {
			state = value + " " + unit
		}
		try {
			device.setNumber(value as float)
		} catch (e) {
		}
	} else {
		//switch, light, fan, input_boolean, ...
	}
	device.setStatus(state)					// 변환된 값
	device.setStatus(value, attributes)		// 원래값, 속성
}

def authError() {
	[error: "Permission denied"]
}

def renderConfig() {
	def configJson = new groovy.json.JsonOutput().toJson([
		name: "HA-Services",
		app_url: apiServerUrl("/api/smartapps/installations/"),
		app_id: app.id,
		access_token: state.accessToken
	])
	def configString = new groovy.json.JsonOutput().prettyPrint(configJson)
	render contentType: "text/plain", data: configString
}

def getList() {
	def haDevices = []
	def childDevices = getAllChildDevices()
	childDevices.each { childDevice->
		haDevices.push(childDevice.deviceNetworkId)
	}
	def deviceJson = new groovy.json.JsonOutput().toJson([list: haDevices])
	render contentType: "application/json", data: deviceJson
}

mappings {
	if (!params.access_token || (params.access_token && params.access_token != state.accessToken)) {
		path("/config")	{ action: [GET: "authError"] }
		path("/device")	{ action: [GET: "authError"] }
		path("/sensor")	{ action: [GET: "authError"] }
		path("/list") { action: [GET: "authError"] }
	} else {
		path("/config")	{ action: [GET: "renderConfig"] }
		path("/device")	{ action: [GET: "updateDevice"] }
		path("/sensor")	{ action: [GET: "updateSensor"] }
		path("/list") { action: [GET: "getList"] }
	}
}
