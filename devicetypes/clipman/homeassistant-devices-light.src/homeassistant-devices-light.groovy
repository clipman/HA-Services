/**
 *  HomeAssistant Devices (Light) v2022-06-23
 *  clipman@naver.com
 *  날자
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

metadata {
	definition (name: "HomeAssistant Devices (Light)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.light", mnmn: "SmartThingsCommunity", vid: "4c99b003-6212-3ed5-8a92-a49bfbe1095d") {
		capability "Switch"
		capability "Color Control"
		capability "Switch Level"
		capability "Refresh"
	}
}

def setEntityStatus(state) {
	sendEvent(name: "switch", value: state)
}

def setEntityStatus(state, attributes) {
	log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	if(attributes["brightness"] != null){
		sendEvent(name: "level", value: Math.round(attributes.brightness*100/255) as int)
	}
	if(attributes["rgb_color"] != null){
		String hex = String.format("#%02x%02x%02x", attributes.rgb_color[0], attributes.rgb_color[1], attributes.rgb_color[2]);  
		sendEvent(name:"color", value: hex )

		def hsv = rgbToHSV(attributes.rgb_color[0], attributes.rgb_color[1], attributes.rgb_color[2])
		sendEvent(name: "saturation", value: hsv.saturation)
		sendEvent(name: "hue", value: hsv.hue)
	}
}

def on() {
	control("on")
}

def off() {
	control("off")
}

def installed() {
	state.hue = 0
	state.saturation = 0
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}

def control(onOff) {
	parent.services("/api/services/homeassistant/turn_" + onOff, ["entity_id": device.deviceNetworkId])
}

def setColor(color){
    state.hue = color.hue
    state.saturation = color.saturation
	def rgb = huesatToRGB(color.hue, color.saturation)
	parent.services("/api/services/homeassistant/turn_on", ["entity_id": device.deviceNetworkId, "rgb_color": [rgb[0], rgb[1], rgb[2]]])
}
def setHue(hue){
    state.hue = hue
}
def setSaturation(saturation){
    state.saturation = saturation
	if(state.hue != null){
		def rgb = huesatToRGB(state.hue, state.saturation)
		parent.services("/api/services/homeassistant/turn_on", ["entity_id": device.deviceNetworkId, "rgb_color": [rgb[0], rgb[1], rgb[2]]])
	}
}

def setLevel(level){
	def brightness = Math.round(level*255/100) as int
	parent.services("/api/services/homeassistant/turn_on", ["entity_id": device.deviceNetworkId, "brightness": brightness])
}

def rgbToHSV(red, green, blue) {
	def hex = colorUtil.rgbToHex(red as int, green as int, blue as int)
	def hsv = colorUtil.hexToHsv(hex)
	return [hue: hsv[0], saturation: hsv[1], value: hsv[2]]
}

def huesatToRGB(hue, sat) {
	def color = colorUtil.hsvToHex(Math.round(hue) as int, Math.round(sat) as int)
	return colorUtil.hexToRgb(color)
}
