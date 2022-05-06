/**
 *  HA-Publish v2022-05-01
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
 *
 */

import groovy.transform.Field

@Field
CAPABILITY_MAP = [
	"accelerationSensor": [
		name: "Acceleration Sensor",
		capability: "capability.accelerationSensor",
		attributes: [
			"acceleration"
		]
	],
	"activityLightingMode": [
		name: "Activity Lighting Mode",
		capability: "capability.activityLightingMode",
		attributes: [
			"lightingMode"
		]
	],
	"activitySensor": [
		name: "Activity Sensor",
		capability: "capability.activitySensor",
		attributes: [
			"activity"
		]
	],
	"airConditionerFanMode": [
		name: "Air Conditioner Fan Mode",
		capability: "capability.airConditionerFanMode",
		attributes: [
			"fanMode"
		]
	],
	"airConditionerMode": [
		name: "Air Conditioner Mode",
		capability: "capability.airConditionerMode",
		attributes: [
			"airConditionerMode"
		]
	],
	"airPurifierFanMode": [
		name: "Air Purifier Fan Mode",
		capability: "capability.airPurifierFanMode",
		attributes: [
			"airPurifierFanMode"
		]
	],
	"airQualitySensor": [
		name: "Air Quality Sensor",
		capability: "capability.airQualitySensor",
		attributes: [
			"airQuality"
		]
	],
/*
	"alarm": [
		name: "Alarm",
		capability: "capability.alarm",
		attributes: [
			"alarm"
		]
	],
	"atmosphericPressureMeasurement": [
		name: "Atmospheric Pressure Measurement",
		capability: "capability.atmosphericPressureMeasurement",
		attributes: [
			"atmosphericPressure"
		]
	],
	"audioCapture": [
		name: "Audio Capture",
		capability: "capability.audioCapture",
		attributes: [
			"stream",
			"clip"
		]
	],
	"audioMute": [
		name: "Audio Mute",
		capability: "capability.audioMute",
		attributes: [
			"mute"
		]
	],
	"audioStream": [
		name: "Audio Stream",
		capability: "capability.audioStream",
		attributes: [
			"uri"
		]
	],
	"audioTrackData": [
		name: "Audio Track Data",
		capability: "capability.audioTrackData",
		attributes: [
			"totalTime",
			"audioTrackData",
			"elapsedTime"
		]
	],
	"audioVolume": [
		name: "Audio Volume",
		capability: "capability.audioVolume",
		attributes: [
			"volume"
		]
	],
*/
	"battery": [
		name: "Battery",
		capability: "capability.battery",
		attributes: [
			"battery"
		]
	],
/*
	"bodyMassIndexMeasurement": [
		name: "Body Mass Index Measurement",
		capability: "capability.bodyMassIndexMeasurement",
		attributes: [
			"bmiMeasurement"
		]
	],
	"bodyWeightMeasurement": [
		name: "Body Weight Measurement",
		capability: "capability.bodyWeightMeasurement",
		attributes: [
			"bodyWeightMeasurement"
		]
	],
*/
	"button": [
		name: "Button",
		capability: "capability.button",
		attributes: [
			"button",
			"numberOfButtons",
			"supportedButtonValues"
		]
	],
/*
	"bypassable": [
		name: "Bypassable",
		capability: "capability.bypassable",
		attributes: [
			"bypassStatus"
		]
	],
	"cameraPreset": [
		name: "Camera Preset",
		capability: "capability.cameraPreset",
		attributes: [
			"presets"
		]
	],
*/
	"carbonDioxideHealthConcern": [
		name: "Carbon Dioxide Health Concern",
		capability: "capability.carbonDioxideHealthConcern",
		attributes: [
			"carbonDioxideHealthConcern"
		]
	],
	"carbonDioxideMeasurement": [
		name: "Carbon Dioxide Measurement",
		capability: "capability.carbonDioxideMeasurement",
		attributes: [
			"carbonDioxide"
		]
	],
	"carbonMonoxideDetector": [
		name: "Carbon Monoxide Detector",
		capability: "capability.carbonMonoxideDetector",
		attributes: [
			"carbonMonoxide"
		]
	],
	"carbonMonoxideMeasurement": [
		name: "Carbon Monoxide Measurement",
		capability: "capability.carbonMonoxideMeasurement",
		attributes: [
			"carbonMonoxideLevel"
		]
	],
/*
	"chime": [
		name: "Chime",
		capability: "capability.chime",
		attributes: [
			"chime"
		]
	],
	"color": [
		name: "Color",
		capability: "capability.color",
		attributes: [
			"colorValue"
		]
	],
	"colorControl": [
		name: "Color Control",
		capability: "capability.colorControl",
		attributes: [
			"saturation",
			"color",
			"hue"
		]
	],
	"colorMode": [
		name: "Color Mode",
		capability: "capability.colorMode",
		attributes: [
			"colorMode"
		]
	],
	"colorTemperature": [
		name: "Color Temperature",
		capability: "capability.colorTemperature",
		attributes: [
			"colorTemperature"
		]
	],
	"consumable": [
		name: "Consumable",
		capability: "capability.consumable",
		attributes: [
			"consumableStatus"
		]
	],
*/
	"contactSensor": [
		name: "Contact Sensor",
		capability: "capability.contactSensor",
		attributes: [
			"contact"
		]
	],
/*
	"demandResponseLoadControl": [
		name: "Demand Response Load Control",
		capability: "capability.demandResponseLoadControl",
		attributes: [
			"drlcStatus"
		]
	],
	"dewPoint": [
		name: "Dew Point",
		capability: "capability.dewPoint",
		attributes: [
			"dewpoint"
		]
	],
*/
	"dishwasherMode": [
		name: "Dishwasher Mode",
		capability: "capability.dishwasherMode",
		attributes: [
			"dishwasherMode"
		]
	],
	"dishwasherOperatingState": [
		name: "Dishwasher Operating State",
		capability: "capability.dishwasherOperatingState",
		attributes: [
			"completionTime",
			"machineState",
			"supportedMachineStates",
			"dishwasherJobState"
		]
	],
	"doorControl": [
		name: "Door Control",
		capability: "capability.doorControl",
		attributes: [
			"door"
		]
	],
/*
	"drivingStatus": [
		name: "Driving Status",
		capability: "capability.drivingStatus",
		attributes: [
			"drivingStatus"
		]
	],
	"dryerMode": [
		name: "Dryer Mode",
		capability: "capability.dryerMode",
		attributes: [
			"dryerMode"
		]
	],
	"dryerOperatingState": [
		name: "Dryer Operating State",
		capability: "capability.dryerOperatingState",
		attributes: [
			"completionTime",
			"machineState",
			"supportedMachineStates",
			"dryerJobState"
		]
	],
*/
	"dustClass": [
		name: "Dust Class (Custom)",
		capability: "capability.circlecircle06391.dustClass",
		attributes: [
			"dustClass"
		]
	],
	"dustHealthConcern": [
		name: "Dust Health Concern",
		capability: "capability.dustHealthConcern",
		attributes: [
			"dustHealthConcern"
		]
	],
	"dustSensor": [
		name: "Dust Sensor",
		capability: "capability.dustSensor",
		attributes: [
			"dustLevel",
			"fineDustLevel"
		]
	],
	"elevatorCall": [
		name: "Elevator Call",
		capability: "capability.elevatorCall",
		attributes: [
			"callStatus"
		]
	],
	"energyMeter": [
		name: "Energy Meter",
		capability: "capability.energyMeter",
		attributes: [
			"energy"
		]
	],
	"equivalentCarbonDioxideMeasurement": [
		name: "Equivalent Carbon Dioxide Measurement",
		capability: "capability.equivalentCarbonDioxideMeasurement",
		attributes: [
			"equivalentCarbonDioxideMeasurement"
		]
	],
/*
	"estimatedTimeOfArrival": [
		name: "Estimated Time Of Arrival",
		capability: "capability.estimatedTimeOfArrival",
		attributes: [
			"eta"
		]
	],
*/
	"execute": [
		name: "Execute",
		capability: "capability.execute",
		attributes: [
			"data"
		]
	],
	"fanOscillationMode": [
		name: "Fan Oscillation Mode",
		capability: "capability.fanOscillationMode",
		attributes: [
			"supportedFanOscillationModes",
			"fanOscillationMode"
		]
	],
	"fanSpeed": [
		name: "Fan Speed",
		capability: "capability.fanSpeed",
		attributes: [
			"fanSpeed"
		]
	],
/*
	"feederOperatingState": [
		name: "Feeder Operating State",
		capability: "capability.feederOperatingState",
		attributes: [
			"feederOperatingState"
		]
	],
	"feederPortion": [
		name: "Feeder Portion",
		capability: "capability.feederPortion",
		attributes: [
			"feedPortion"
		]
	],
*/
	"filterState": [
		name: "Filter State",
		capability: "capability.filterState",
		attributes: [
			"filterLifeRemaining"
		]
	],
	"filterStatus": [
		name: "Filter Status",
		capability: "capability.filterStatus",
		attributes: [
			"filterStatus"
		]
	],
	"fineDustClass": [
		name: "Fine Dust Class (Custom)",
		capability: "capability.circlecircle06391.fineDustClass",
		attributes: [
			"fineDustClass"
		]
	],
	"fineDustHealthConcern": [
		name: "Fine Dust Health Concern",
		capability: "capability.fineDustHealthConcern",
		attributes: [
			"fineDustHealthConcern"
		]
	],
	"fineDustSensor": [
		name: "Fine Dust Sensor",
		capability: "capability.fineDustSensor",
		attributes: [
			"fineDustLevel"
		]
	],
/*
	"firmwareUpdate": [
		name: "Firmware Update",
		capability: "capability.firmwareUpdate",
		attributes: [
			"lastUpdateStatusReason",
			"availableVersion",
			"lastUpdateStatus",
			"state",
			"currentVersion",
			"lastUpdateTime"
		]
	],
	"formaldehydeMeasurement": [
		name: "Formaldehyde Measurement",
		capability: "capability.formaldehydeMeasurement",
		attributes: [
			"formaldehydeLevel"
		]
	],
	"garageDoorControl": [
		name: "Garage Door Control",
		capability: "capability.garageDoorControl",
		attributes: [
			"door"
		]
	],
	"gasDetector": [
		name: "Gas Detector",
		capability: "capability.gasDetector",
		attributes: [
			"gas"
		]
	],
	"gasMeter": [
		name: "Gas Meter",
		capability: "capability.gasMeter",
		attributes: [
			"gasMeterPrecision",
			"gasMeterCalorific",
			"gasMeterTime",
			"gasMeterVolume",
			"gasMeterConversion",
			"gasMeter"
		]
	],
	"geofence": [
		name: "Geofence",
		capability: "capability.geofence",
		attributes: [
			"enableState",
			"geofence",
			"name"
		]
	],
	"geolocation": [
		name: "Geolocation",
		capability: "capability.geolocation",
		attributes: [
			"method",
			"heading",
			"latitude",
			"accuracy",
			"altitudeAccuracy",
			"speed",
			"longitude",
			"lastUpdateTime"
		]
	],
	"healthCheck": [
		name: "Health Check",
		capability: "capability.healthCheck",
		attributes: [
			"checkInterval",
			"healthStatus",
			"DeviceWatch-Enroll",
			"DeviceWatch-DeviceStatus"
		]
	],
*/
	"humidifierMode": [
		name: "Humidifier Mode",
		capability: "capability.humidifierMode",
		attributes: [
			"humidifierMode"
		]
	],
	"illuminanceMeasurement": [
		name: "Illuminance Measurement",
		capability: "capability.illuminanceMeasurement",
		attributes: [
			"illuminance"
		]
	],
/*
	"imageCapture": [
		name: "Image Capture",
		capability: "capability.imageCapture",
		attributes: [
			"image",
			"encrypted",
			"captureTime"
		]
	],
	"infraredLevel": [
		name: "Infrared Level",
		capability: "capability.infraredLevel",
		attributes: [
			"infraredLevel"
		]
	],
	"languageSetting": [
		name: "Language Setting",
		capability: "capability.languageSetting",
		attributes: [
			"supportedLanguages",
			"language"
		]
	],
	"locationMode": [
		name: "Location Mode",
		capability: "capability.locationMode",
		attributes: [
			"mode"
		]
	],
*/
	"lock": [
		name: "Lock",
		capability: "capability.lock",
		attributes: [
			"lock"
		]
	],
/*
	"lockCodes": [
		name: "Lock Codes",
		capability: "capability.lockCodes",
		attributes: [
			"codeLength",
			"maxCodes",
			"maxCodeLength",
			"codeChanged",
			"lock",
			"minCodeLength",
			"codeReport",
			"scanCodes",
			"lockCodes"
		]
	],
	"logTrigger": [
		name: "Log Trigger",
		capability: "capability.logTrigger",
		attributes: [
			"logState",
			"logRequestState",
			"logInfo"
		]
	],
	"mediaController": [
		name: "Media Controller",
		capability: "capability.mediaController",
		attributes: [
			"currentActivity",
			"activities"
		]
	],
	"mediaGroup": [
		name: "Media Group",
		capability: "capability.mediaGroup",
		attributes: [
			"groupMute",
			"groupPrimaryDeviceId",
			"groupId",
			"groupVolume",
			"groupRole"
		]
	],
	"mediaInputSource": [
		name: "Media Input Source",
		capability: "capability.mediaInputSource",
		attributes: [
			"supportedInputSources",
			"inputSource"
		]
	],
	"mediaPlayback": [
		name: "Media Playback",
		capability: "capability.mediaPlayback",
		attributes: [
			"supportedPlaybackCommands",
			"playbackStatus"
		]
	],
	"mediaPlaybackRepeat": [
		name: "Media Playback Repeat",
		capability: "capability.mediaPlaybackRepeat",
		attributes: [
			"playbackRepeatMode"
		]
	],
	"mediaPlaybackShuffle": [
		name: "Media Playback Shuffle",
		capability: "capability.mediaPlaybackShuffle",
		attributes: [
			"playbackShuffle"
		]
	],
	"mediaPresets": [
		name: "Media Presets",
		capability: "capability.mediaPresets",
		attributes: [
			"presets"
		]
	],
*/
	"mediaTrackControl": [
		name: "Media Track Control",
		capability: "capability.mediaTrackControl",
		attributes: [
			"supportedTrackControlCommands"
		]
	],
/*
	"mode": [
		name: "Mode",
		capability: "capability.mode",
		attributes: [
			"mode",
			"supportedModes"
		]
	],
	"moldHealthConcern": [
		name: "Mold Health Concern",
		capability: "capability.moldHealthConcern",
		attributes: [
			"moldHealthConcern"
		]
	],
*/
	"motionSensor": [
		name: "Motion Sensor",
		capability: "capability.motionSensor",
		attributes: [
			"motion"
		]
	],
/*
	"musicPlayer": [
		name: "Music Player",
		capability: "capability.musicPlayer",
		attributes: [
			"trackDescription",
			"level",
			"mute",
			"trackData",
			"status"
		]
	],
*/
	"odorClass": [
		name: "Odor Sensor Class (Custom)",
		capability: "capability.circlecircle06391.odorClass",
		attributes: [
			"odorClass"
		]
	],
	"odorSensor": [
		name: "Odor Sensor",
		capability: "capability.odorSensor",
		attributes: [
			"odorLevel"
		]
	],
	"ovenMode": [
		name: "Oven Mode",
		capability: "capability.ovenMode",
		attributes: [
			"supportedOvenModes",
			"ovenMode"
		]
	],
	"ovenOperatingState": [
		name: "Oven Operating State",
		capability: "capability.ovenOperatingState",
		attributes: [
			"completionTime",
			"machineState",
			"progress",
			"supportedMachineStates",
			"ovenJobState",
			"operationTime"
		]
	],
	"ovenSetpoint": [
		name: "Oven Setpoint",
		capability: "capability.ovenSetpoint",
		attributes: [
			"ovenSetpoint"
		]
	],
/*
	"panicAlarm": [
		name: "Panic Alarm",
		capability: "capability.panicAlarm",
		attributes: [
			"panicAlarm"
		]
	],
	"pestControl": [
		name: "Pest Control",
		capability: "capability.pestControl",
		attributes: [
			"pestControl"
		]
	],
	"powerConsumptionReport": [
		name: "Power Consumption Report",
		capability: "capability.powerConsumptionReport",
		attributes: [
			"powerConsumption"
		]
	],
*/
	"powerMeter": [
		name: "Power Meter",
		capability: "capability.powerMeter",
		attributes: [
			"power"
		]
	],
/*
	"powerSource": [
		name: "Power Source",
		capability: "capability.powerSource",
		attributes: [
			"powerSource"
		]
	],
	"precipitationMeasurement": [
		name: "Precipitation Measurement",
		capability: "capability.precipitationMeasurement",
		attributes: [
			"precipitationLevel"
		]
	],
	"precipitationRate": [
		name: "Precipitation Rate",
		capability: "capability.precipitationRate",
		attributes: [
			"precipitationRate"
		]
	],
	"precipitationSensor": [
		name: "Precipitation Sensor",
		capability: "capability.precipitationSensor",
		attributes: [
			"precipitationIntensity"
		]
	],
*/
	"presenceSensor": [
		name: "Presence Sensor",
		capability: "capability.presenceSensor",
		attributes: [
			"presence"
		]
	],
	"radonHealthConcern": [
		name: "Radon Health Concern",
		capability: "capability.radonHealthConcern",
		attributes: [
			"radonHealthConcern"
		]
	],
	"radonMeasurement": [
		name: "Radon Measurement",
		capability: "capability.radonMeasurement",
		attributes: [
			"radonLevel"
		]
	],
/*
	"rapidCooling": [
		name: "Rapid Cooling",
		capability: "capability.rapidCooling",
		attributes: [
			"rapidCooling"
		]
	],
*/
	"refrigeration": [
		name: "Refrigeration",
		capability: "capability.refrigeration",
		attributes: [
			"defrost",
			"rapidCooling",
			"rapidFreezing"
		]
	],
	"refrigerationSetpoint": [
		name: "Refrigeration Setpoint",
		capability: "capability.refrigerationSetpoint",
		attributes: [
			"refrigerationSetpoint"
		]
	],
	"relativeBrightness": [
		name: "Relative Brightness",
		capability: "capability.relativeBrightness",
		attributes: [
			"brightnessIntensity"
		]
	],
	"relativeHumidityMeasurement": [
		name: "Relative Humidity Measurement",
		capability: "capability.relativeHumidityMeasurement",
		attributes: [
			"humidity"
		]
	],
/*
	"remoteControlStatus": [
		name: "Remote Control Status",
		capability: "capability.remoteControlStatus",
		attributes: [
			"remoteControlEnabled"
		]
	],
*/
	"robotCleanerCleaningMode": [
		name: "Robot Cleaner Cleaning Mode",
		capability: "capability.robotCleanerCleaningMode",
		attributes: [
			"robotCleanerCleaningMode"
		]
	],
	"robotCleanerMovement": [
		name: "Robot Cleaner Movement",
		capability: "capability.robotCleanerMovement",
		attributes: [
			"robotCleanerMovement"
		]
	],
	"robotCleanerTurboMode": [
		name: "Robot Cleaner Turbo Mode",
		capability: "capability.robotCleanerTurboMode",
		attributes: [
			"robotCleanerTurboMode"
		]
	],
	"samsungTV": [
		name: "Samsung TV",
		capability: "capability.samsungTV",
		attributes: [
			"volume",
			"pictureMode",
			"messageButton",
			"soundMode",
			"mute",
			"switch"
		]
	],
/*
	"scent": [
		name: "Scent",
		capability: "capability.scent",
		attributes: [
			"scentName",
			"scentIntensity"
		]
	],
	"securitySystem": [
		name: "Security System",
		capability: "capability.securitySystem",
		attributes: [
			"alarm",
			"securitySystemStatus"
		]
	],
	"signalStrength": [
		name: "Signal Strength",
		capability: "capability.signalStrength",
		attributes: [
			"lqi",
			"rssi"
		]
	],
	"sleepSensor": [
		name: "Sleep Sensor",
		capability: "capability.sleepSensor",
		attributes: [
			"sleeping"
		]
	],
*/
	"smokeDetector": [
		name: "Smoke Detector",
		capability: "capability.smokeDetector",
		attributes: [
			"smoke"
		]
	],
	"soundDetection": [
		name: "Sound Detection",
		capability: "capability.soundDetection",
		attributes: [
			"soundDetectionState",
			"supportedSoundTypes",
			"soundDetected"
		]
	],
	"soundPressureLevel": [
		name: "Sound Pressure Level",
		capability: "capability.soundPressureLevel",
		attributes: [
			"soundPressureLevel"
		]
	],
	"soundSensor": [
		name: "Sound Sensor",
		capability: "capability.soundSensor",
		attributes: [
			"sound"
		]
	],
/*
	"speechRecognition": [
		name: "Speech Recognition",
		capability: "capability.speechRecognition",
		attributes: [
			"phraseSpoken"
		]
	],
*/
	"status": [
		name: "Status (Custom)",
		capability: "capability.circlecircle06391.status",
		attributes: [
			"statusbar"
		]
	],
	"statusBar": [
		name: "Status Bar (Custom)",
		capability: "capability.circlecircle06391.statusBar",
		attributes: [
			"status"
		]
	],
	"stepSensor": [
		name: "Step Sensor",
		capability: "capability.stepSensor",
		attributes: [
			"goal",
			"steps"
		]
	],
	"switch": [
		name: "Switch",
		capability: "capability.switch",
		attributes: [
			"switch"
		]
	],
	"switchLevel": [
		name: "Switch Level",
		capability: "capability.switchLevel",
		attributes: [
			"level"
		]
	],
	"tamperAlert": [
		name: "Tamper Alert",
		capability: "capability.tamperAlert",
		attributes: [
			"tamper"
		]
	],
/*
	"temperatureAlarm": [
		name: "Temperature Alarm",
		capability: "capability.temperatureAlarm",
		attributes: [
			"temperatureAlarm"
		]
	],
*/
	"temperatureMeasurement": [
		name: "Temperature Measurement",
		capability: "capability.temperatureMeasurement",
		attributes: [
			"temperature"
		]
	],
	"thermostatCoolingSetpoint": [
		name: "Thermostat Cooling Setpoint",
		capability: "capability.thermostatCoolingSetpoint",
		attributes: [
			"coolingSetpoint"
		]
	],
	"thermostatFanMode": [
		name: "Thermostat Fan Mode",
		capability: "capability.thermostatFanMode",
		attributes: [
			"thermostatFanMode",
			"supportedThermostatFanModes"
		]
	],
	"thermostatHeatingSetpoint": [
		name: "Thermostat Heating Setpoint",
		capability: "capability.thermostatHeatingSetpoint",
		attributes: [
			"heatingSetpoint"
		]
	],
	"thermostatMode": [
		name: "Thermostat Mode",
		capability: "capability.thermostatMode",
		attributes: [
			"thermostatMode",
			"supportedThermostatModes"
		]
	],
	"thermostatOperatingState": [
		name: "Thermostat Operating State",
		capability: "capability.thermostatOperatingState",
		attributes: [
			"thermostatOperatingState"
		]
	],
	"threeAxis": [
		name: "Three Axis",
		capability: "capability.threeAxis",
		attributes: [
			"threeAxis"
		]
	],
/*
	"timedSession": [
		name: "Timed Session",
		capability: "capability.timedSession",
		attributes: [
			"completionTime",
			"sessionStatus"
		]
	],
	"tvChannel": [
		name: "Tv Channel",
		capability: "capability.tvChannel",
		attributes: [
			"tvChannel",
			"tvChannelName"
		]
	],
*/
	"tvocHealthConcern": [
		name: "Tvoc Health Concern",
		capability: "capability.tvocHealthConcern",
		attributes: [
			"tvocHealthConcern"
		]
	],
	"tvocMeasurement": [
		name: "Tvoc Measurement",
		capability: "capability.tvocMeasurement",
		attributes: [
			"tvocLevel"
		]
	],
	"ultravioletIndex": [
		name: "Ultraviolet Index",
		capability: "capability.ultravioletIndex",
		attributes: [
			"ultravioletIndex"
		]
	],
	"valve": [
		name: "Valve",
		capability: "capability.valve",
		attributes: [
			"valve"
		]
	],
/*
	"vehicleEngine": [
		name: "Vehicle Engine",
		capability: "capability.vehicleEngine",
		attributes: [
			"engineState"
		]
	],
	"vehicleFuelLevel": [
		name: "Vehicle Fuel Level",
		capability: "capability.vehicleFuelLevel",
		attributes: [
			"fuelLevel"
		]
	],
	"vehicleInformation": [
		name: "Vehicle Information",
		capability: "capability.vehicleInformation",
		attributes: [
			"vehicleColor",
			"vehicleYear",
			"vehicleImage",
			"vehicleTrim",
			"vehiclePlate",
			"vehicleModel",
			"vehicleId",
			"vehicleMake"
		]
	],
	"vehicleOdometer": [
		name: "Vehicle Odometer",
		capability: "capability.vehicleOdometer",
		attributes: [
			"odometerReading"
		]
	],
	"vehicleRange": [
		name: "Vehicle Range",
		capability: "capability.vehicleRange",
		attributes: [
			"estimatedRemainingRange"
		]
	],
	"vehicleTirePressureMonitor": [
		name: "Vehicle Tire Pressure Monitor",
		capability: "capability.vehicleTirePressureMonitor",
		attributes: [
			"tirePressureState"
		]
	],
*/
	"veryFineDustClass": [
		name: "Very Fine Dust Sensor Class (Custom)",
		capability: "capability.circlecircle06391.veryFineDustClass",
		attributes: [
			"veryFineDustClass"
		]
	],
	"veryFineDustHealthConcern": [
		name: "Very Fine Dust Health Concern",
		capability: "capability.veryFineDustHealthConcern",
		attributes: [
			"veryFineDustHealthConcern"
		]
	],
	"veryFineDustSensor": [
		name: "Very Fine Dust Sensor",
		capability: "capability.veryFineDustSensor",
		attributes: [
			"veryFineDustLevel"
		]
	],
	"videoCamera": [
		name: "Video Camera",
		capability: "capability.videoCamera",
		attributes: [
			"settings",
			"mute",
			"camera",
			"statusMessage"
		]
	],
	"videoCapture": [
		name: "Video Capture",
		capability: "capability.videoCapture",
		attributes: [
			"stream",
			"clip"
		]
	],
	"videoClips": [
		name: "Video Clips",
		capability: "capability.videoClips",
		attributes: [
			"videoClip"
		]
	],
	"videoStream": [
		name: "Video Stream",
		capability: "capability.videoStream",
		attributes: [
			"stream"
		]
	],
	"voltageMeasurement": [
		name: "Voltage Measurement",
		capability: "capability.voltageMeasurement",
		attributes: [
			"voltage"
		]
	],
	"washerMode": [
		name: "Washer Mode",
		capability: "capability.washerMode",
		attributes: [
			"washerMode"
		]
	],
	"washerOperatingState": [
		name: "Washer Operating State",
		capability: "capability.washerOperatingState",
		attributes: [
			"completionTime",
			"machineState",
			"washerJobState",
			"supportedMachineStates"
		]
	],
	"waterSensor": [
		name: "Water Sensor",
		capability: "capability.waterSensor",
		attributes: [
			"water"
		]
	],
/*
	"wifiMeshRouter": [
		name: "Wifi Mesh Router",
		capability: "capability.wifiMeshRouter",
		attributes: [
			"disconnectedRouterCount",
			"wifiGuestNetworkStatus",
			"connectedRouterCount",
			"connectedDeviceCount",
			"wifiNetworkName",
			"wifiGuestNetworkName",
			"wifiNetworkStatus"
		]
	],
	"windSpeed": [
		name: "Wind Speed",
		capability: "capability.windSpeed",
		attributes: [
			"windspeed"
		]
	],
	"windowShade": [
		name: "Window Shade",
		capability: "capability.windowShade",
		attributes: [
			"supportedWindowShadeCommands",
			"windowShade"
		]
	],
	"windowShadeLevel": [
		name: "Window Shade Level",
		capability: "capability.windowShadeLevel",
		attributes: [
			"shadeLevel"
		]
	],
	"wirelessOperatingMode": [
		name: "Wireless Operating Mode",
		capability: "capability.wirelessOperatingMode",
		attributes: [
			"wirelessOperatingMode"
		]
	],
	"zwMultichannel": [
		name: "Zw Multichannel",
		capability: "capability.zwMultichannel",
		attributes: [
			"epEvent",
			"epInfo"
		]
	],
	"pHMeasurement": [
		name: "pH Measurement",
		capability: "capability.pHMeasurement",
		attributes: [
			"pH"
		]
	],
	"webrtc": [
		name: "webrtc",
		capability: "capability.webrtc",
		attributes: [
			"sdpAnswer",
			"talkback",
			"supportedFeatures",
			"audioOnly",
			"stunUrl"
		]
	],
*/
	"pressure": [
		name: "Pressure (Custom)",
		capability: "capability.circlecircle06391.pressure",
		attributes: ["pressure"]
	],
	"pressureTrend": [
		name: "Pressure Trend (Custom)",
		capability: "capability.circlecircle06391.pressureTrend",
		attributes: ["pressureTrend"]
	],
	"rainfallProbability": [
		name: "Rainfall Probability (Custom)",
		capability: "capability.circlecircle06391.precipChance",
		attributes: ["precipChance"]
	],
	"visibility": [
		name: "Visibility (Custom)",
		capability: "capability.circlecircle06391.visibility",
		attributes: ["visibility"]
	],
	"waterSensor_Custom": [
		name: "Water Sensor (Custom)",
		capability: "capability.borderreason25422.waterSensor",
		attributes: ["water"]
	],
	"weatherForecast": [
		name: "Weather Forecast (Custom)",
		capability: "capability.circlecircle06391.weatherforecast",
		attributes: ["weatherForecast"]
	],
	"windSpeed_Custom": [
		name: "Wind Speed (Custom)",
		capability: "capability.circlecircle06391.windspeed",
		attributes: ["windSpeed"]
	],
	"windBearing": [
		name: "Wind Bearing (Custom)",
		capability: "capability.circlecircle06391.windbearing",
		attributes: ["windBearing"]
	]
]

definition(
	name: "HA-Publish",
	namespace: "clipman",
	author: "clipman",
	description: "SmartThings의 센서값이 변경되면 MQTT Publish",
	category: "My Apps",
	iconUrl: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX2Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	iconX3Url: "https://brands.home-assistant.io/_/ha_services/icon.png",
	oauth: true
)

preferences {
   page(name: "mainPage")
   page(name: "collectorDevicePage")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "MQTT Publish", nextPage: null, uninstall: true, install: true) {
		section("HomeAssistant 연결 설정") {
			paragraph "https://xxx.duckdns.org 또는 http://xxx.duckdns.org:8123"
			input "haURL", "text", title: "HomeAssistant URL", required: true
			input "haToken", "text", title: "HomeAssistant Token", required: true
		}
		section("MQTT Publish") {
		   href "collectorDevicePage", title: "MQTT Publish할 속성들", description:"선택한 속성이 변하면 그 값을 MQTT Publish 합니다."
		}
		section() {
			href url:"${apiServerUrl("/api/smartapps/installations/${app.id}/config?access_token=${state.accessToken}")}", style:"embedded", required:false, title:"HomeAssistant 설정 정보 읽어오기", description:"누르고 선택하고 복사한 후에 \"완료\"를 누르세요."
		}
		section() {
			label title: "앱의 이름 변경 (선택사항)", description: "앱의 이름을 바꿀 수 있어요", defaultValue: app?.name, required: false
		}
	}
}

def collectorDevicePage() {
	dynamicPage(name: "collectorDevicePage", title: "") {
		section("이 값들이 변경되면 MQTT Publish 합니다.") {
			CAPABILITY_MAP.each { key, capability ->
				input key, capability["capability"], title: capability["name"], multiple: true, required: false
			}
		}
	}
}

def installed() {
	initialize()
}

def initialize() {
	if (!state.accessToken) {
		createAccessToken()
	}
}

def updated() {
	log.info "Updated with settings: ${settings}"
	initialize()
	unsubscribe()

	CAPABILITY_MAP.each { key, capability ->
		capability["attributes"].each { attribute ->
			for (device in settings[key]) {
				if(settings[key]){
					subscribe(device, attribute, stateChangeHandler)
					//log.debug "subscribe(device={$device}, attribute={$attribute}, stateChangeHandler={$stateChangeHandler})"
				}
			}
		}
	}

	def devices = []
	CAPABILITY_MAP.each { key, capability ->
		for (device in settings[key]) {
			def dni = device.deviceNetworkId
			if(!existDeviceInList(devices, dni)){
				devices.push(dni)
                publishDevice(device)
			}
		}
	}
}

def existDeviceInList(devices, dni) {
	for (item in devices) {
		if(item == dni) {
			return true
		}
	}
	return false
}

/*
//이벤트가 발생한 디바이스의 속성을 MQTT로 Publish한다.
def stateChangeHandler(evt) {
	def device = evt.getDevice()
	def data = [:]
	//data["id"] = device.id
	//data["dni"] = device.deviceNetworkId
	//data["name"] = device.name
	//data["label"] = evt.displayName
	//data["attribute"] = evt.name
	data["value"] = device.currentValue(evt.name)
	data["${evt.name}"] = device.currentValue(evt.name)
	def payload = new groovy.json.JsonOutput().toJson(data)
	services("/api/services/mqtt/publish", ["topic": "/smartthings/"+device.deviceNetworkId+"/"+evt.name, "payload": "'" + payload.toString() + "'"])
	log.debug "stateChangeHandler: ${device.deviceNetworkId}/${evt.name}, " + payload.toString()
}
*/
/*
//이벤트가 발생한 디바이스의 속성을 MQTT로 Publish한다.
def stateChangeHandler(evt) {
	def device = evt.getDevice()
	services("/api/services/mqtt/publish", ["topic": "/smartthings/"+device.deviceNetworkId+"/"+evt.name, "payload": '"' + device.currentValue(evt.name) + '"'])
	log.debug "MQTT Publish: /smartthings/${device.deviceNetworkId}/${evt.name}, " + device.currentValue(evt.name)
}
*/
/*
//이벤트가 발생한 디바이스의 모든 속성을 MQTT로 Publish한다.
def stateChangeHandler(evt) {
	def device = evt.getDevice()
	def data = [:]
	//data["id"] = device.id
	//data["dni"] = device.deviceNetworkId
	data["name"] = device.name
	//data["label"] = evt.displayName
	//data["attribute"] = evt.name
	//data["value"] = device.currentValue(evt.name)
	data["update"] = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)

	def attributes = device.supportedAttributes
	attributes.each { attribute ->
		try {
			def val = device.currentValue(attribute.name)
			if(val != null) {
		   		data["${attribute.name}"] = val
			}
		} catch(e) {
			log.error("${e} --> ${attribute.name}")
		}
	}
	def payload = new groovy.json.JsonOutput().toJson(data)
	services("/api/services/mqtt/publish", ["topic": "/smartthings/"+device.deviceNetworkId+"/"+evt.name, "payload": "'" + payload.toString() + "'"])
	log.debug "stateChangeHandler: ${device.deviceNetworkId}/${evt.name}, " + payload.toString()
}
*/
//이벤트가 발생한 디바이스의 모든 속성을 MQTT로 Publish한다.
def stateChangeHandler(evt) {
	publishDevice(evt.getDevice())
}

//전달한 디바이스의 모든 속성들을 MQTT로 Publish한다.
def publishDevice(device) {
	def data = [:]
	data["name"] = device.name
	data["update"] = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)

	def attributes = device.supportedAttributes
	attributes.each { attribute ->
		try {
			def val = device.currentValue(attribute.name)
			if(val != null) {
		   		data["${attribute.name}"] = val
			}
		} catch(e) {
			log.error("${e} --> ${attribute.name}")
		}
	}
	def payload = new groovy.json.JsonOutput().toJson(data)
	services("/api/services/mqtt/publish", ["topic": "/smartthings/"+device.deviceNetworkId, "payload": "'" + payload.toString() + "'"])
	log.debug "publishDevice: ${device.deviceNetworkId}/, " + payload.toString()
}

def publishDevices() {
	def devices = []
	CAPABILITY_MAP.each { key, capability ->
		for (device in settings[key]) {
			def dni = device.deviceNetworkId
			if(!existDeviceInList(devices, dni)){
				devices.push(dni)
                publishDevice(device)
			}
		}
	}
	def published = new groovy.json.JsonOutput().toJson(devices)
	render contentType: "text/html", data: published
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

def authError() {
	[error: "Permission denied"]
}

/*
def renderConfig() {
	def configJson = new groovy.json.JsonOutput().toJson([
		name: "HA-Publish",
		app_url: apiServerUrl("/api/smartapps/installations/"),
		app_id: app.id,
		access_token: state.accessToken
	])
	def configString = new groovy.json.JsonOutput().prettyPrint(configJson)
	render contentType: "text/plain", data: configString
}
*/
def renderConfig() {
	def configString = 
		"#configuration.yaml\n" +
		"rest_command:\n" +
		"  publish_smartthings_devices:\n" +
		"    url: https://graph-ap02-apnortheast2.api.smartthings.com/api/smartapps/installations/$app.id/publish?access_token=$state.accessToken\n" +
		"    method: GET\n" +
		"\n" +
		"#자동화 참고\n" +
		"trigger:\n" +
		"  - platform: homeassistant\n" +
		"    event: start\n" +
		"action:\n" +
		"  - service: rest_command.publish_smartthings_devices\n" +
		"    data: {}\n"

	render contentType: "text/plain", data: configString
}

mappings {
	if (!params.access_token || (params.access_token && params.access_token != state.accessToken)) {
		path("/config")	{ action: [GET: "authError"] }
		path("/publish") { action: [GET: "authError"] }
	} else {
		path("/config")	{ action: [GET: "renderConfig"] }
		path("/publish") { action: [GET: "publishDevices"] }
	}
}
