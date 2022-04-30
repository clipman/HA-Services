/**
 *  HomeAssistant Devices (Washer) v2022-04-30
 *  clipman@naver.com
 *  날자
 *  가상스위치를 이용한 세탁기
 *  sensor.setaggi_power, sensor.setaggi_energy를 읽어와서 적용
 
#configuration.yaml
switch: !include switchs.yaml

input_boolean:
  washer:
    name: 세탁기

rest_command:
  #세탁기 리프레쉬(세탁기 Power, Energy가 변경되면 SmartThings에 등록된 세탁기 정보를 새로고침)
  refresh_washer:
    url: https://api.smartthings.com/v1/devices/xxx/commands
    method: POST
    headers:
      authorization: 'Bearer xxx'
      content-type: 'application/json'
    payload: |
      [{"component": "main","capability": "refresh","command": "refresh","arguments": []}]

#switchs.yaml
- platform: template
  switches:
    washer:
      friendly_name: 세탁기
      value_template: "{{ is_state('input_boolean.washer', 'on') }}"
      turn_on:
        - service: input_boolean.turn_on
          entity_id: input_boolean.washer
      turn_off:
        - service: input_boolean.turn_off
          entity_id: input_boolean.washer

#automations.yaml
  alias: 세탁기 시작/종료 알림
  description: ''
  trigger:
  - platform: state
    entity_id: switch.washer
    to: 'on'
    from: 'off'
    id: 시작
  - platform: state
    entity_id: switch.washer
    to: 'off'
    from: 'on'
    id: 종료
  - platform: numeric_state
    entity_id: sensor.setaggi_power
    for:
      hours: 0
      minutes: 1
      seconds: 1
    above: '5'
    id: 'ON'
  - platform: numeric_state
    entity_id: sensor.setaggi_power
    for:
      hours: 0
      minutes: 2
      seconds: 1
    id: 'OFF'
    below: '5'
  condition: []
  action:
  - choose:
    - conditions:
      - condition: trigger
        id: 시작
      sequence:
      - service: input_datetime.set_datetime
        entity_id: input_datetime.washer_start
        data:
          timestamp: '{{ as_timestamp(now()) }}'
      - service: rest_command.bixby_computer_speak
        data:
          message: 세탁을 시작했어요.
    - conditions:
      - condition: trigger
        id: 종료
      sequence:
      - service: rest_command.bixby_computer_speak
        data:
          message: 세탁을 완료했어요. 세탁소요시간은 {{ (as_timestamp(now()) - as_timestamp(states('input_datetime.washer_start')))|timestamp_custom('%-H시간%-M분',false)|replace("0시간",
            "") }}입니다.
      - service: rest_command.bixby_livingroom_speak
        data:
          message: 세탁을 완료했어요. 세탁소요시간은 {{ (as_timestamp(now()) - as_timestamp(states('input_datetime.washer_start')))|timestamp_custom('%-H시간%-M분',false)|replace("0시간",
            "") }}입니다.
    - conditions:
      - condition: trigger
        id: 'ON'
      - condition: state
        entity_id: switch.washer
        state: 'off'
      sequence:
      - service: switch.turn_on
        data: {}
        target:
          entity_id: switch.washer
    - conditions:
      - condition: trigger
        id: 'OFF'
      - condition: state
        entity_id: switch.washer
        state: 'on'
      sequence:
      - service: switch.turn_off
        data: {}
        target:
          entity_id: switch.washer
    default: []
  mode: queued

  alias: SmartThings Refresh
  description: ''
  trigger:
  - platform: state
    entity_id: sensor.setaggi_power, sensor.setaggi_energy
    id: refresh_washer
  condition: []
  action:
  - choose:
    - conditions:
      - condition: trigger
        id: refresh_washer
      sequence:
      - service: rest_command.refresh_washer
        data: {}
    default: []
  mode: restart

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
	definition (name: "HomeAssistant Devices (Washer)", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.washer") {
		capability "Switch"
		capability "Power Meter"
		capability "Energy Meter"
		capability "Refresh"
	}
}

def setEntityStatus(value) {
	state.switch = value
	sendEvent(name: "switch", value: value)
}

def setEntityStatus(state, attributes) {
	log.debug "setEntityStatus(state, attributes) : ${state}, ${attributes}"
	def entity
	if(attributes["power"] != null){
		sendEvent(name: "power", value:  attributes["power"] as double, unit: "W", displayed: true)
	} else {
        entity = parent.getEntityStatus("sensor.setaggi_power")
        sendEvent(name: "power", value: entity.state, unit: entity.unit, displayed: true)
	}
	if(attributes["energy"] != null){
		sendEvent(name: "energy", value: attributes["energy"] as double, unit: "kWh", displayed: true)
	} else {
        entity = parent.getEntityStatus("sensor.setaggi_energy")
        sendEvent(name: "energy", value: entity.state, unit: entity.unit, displayed: true)
	}
}

def on() {
	sendEvent(name: "switch", value: state.switch)
}

def off() {
	sendEvent(name: "switch", value: state.switch)
}

def installed() {
	state.switch = "off"
	sendEvent(name: "power", value: 0, unit: "W", displayed: true)
	sendEvent(name: "energy", value: 0, unit: "kWh", displayed: true)
	refresh()
}

def refresh() {
	parent.updateEntity(device.deviceNetworkId)
}