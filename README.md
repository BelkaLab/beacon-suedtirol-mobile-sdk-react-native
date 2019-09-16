# react-native-beacon-suedtirol-mobile-sdk

## Getting started

`$ npm install react-native-beacon-suedtirol-mobile-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-beacon-suedtirol-mobile-sdk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-beacon-suedtirol-mobile-sdk` and add `BeaconSuedtirolMobileSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libBeaconSuedtirolMobileSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.BeaconSuedtirolMobileSdkPackage;` to the imports at the top of the file
  - Add `new BeaconSuedtirolMobileSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-beacon-suedtirol-mobile-sdk'
  	project(':react-native-beacon-suedtirol-mobile-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-beacon-suedtirol-mobile-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-beacon-suedtirol-mobile-sdk')
  	```


## Usage
```javascript
import BeaconSuedtirolMobileSdk from 'react-native-beacon-suedtirol-mobile-sdk';

// TODO: What to do with the module?
BeaconSuedtirolMobileSdk;
```
