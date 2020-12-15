var PLUGIN_NAME = "CordovaAdvanceAILiveness";
var BUILD_GRADLE_PATH = "./platforms/android/app/build.gradle";

var deferral, fs, path;

function log(message) {
    console.log(PLUGIN_NAME + ": " + message);
}

function onFatalException(ex){
    log("EXCEPTION: " + ex.toString());
    deferral.resolve(); // resolve instead of reject so build doesn't fail
}

function run() {
  try {
      fs = require('fs');
      // path = require('path');
  } catch (e) {
      throw("Failed to load dependencies: " + e.toString());
  }
  var buildGradle = fs.readFileSync(BUILD_GRADLE_PATH).toString(),

  let search_str = "dirs project(':CordovaAdvanceAILiveness:liveness').file('libs')";
  let str = "repositories {    flatDir {        dirs project(':CordovaAdvanceAILiveness:liveness').file('libs')    }  }"
  if (buildGradle.indexOf(search_str) == -1){
    //没有这个字符串
    log("try add liveness to build gradle.");
    buildGradle += "\n" + str + "\n";//末尾添加
    fs.writeFileSync(BUILD_GRADLE_PATH, buildGradle, 'utf8');
  }
  ////运行结束
  deferral.resolve();
}

function attempt(fn) {
    return function () {
        try {
            fn.apply(this, arguments);
        } catch (e) {
            onFatalException(e);
        }
    }
}

module.exports = function (ctx) {
    try{
        deferral = require('q').defer();
    }catch(e){
        e.message = 'Unable to load node module dependency \'q\': '+e.message;
        onFatalException(e);
        throw e;
    }
    attempt(run)();
    return deferral.promise;
};