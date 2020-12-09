var exec = require('cordova/exec');

let CordovaAdvanceAILiveness = {
  //初始化SDK
  initLiveness(arg0, success, error){
    exec(success, error, 'CordovaAdvanceAILiveness', 'initLiveness', [arg0]);
  },
  
  //设置License，需要服务端调用获取License，然后客户端设置这个值
  setLicenseAndCheck(arg0, success, error){
    exec(success, error, 'CordovaAdvanceAILiveness', 'setLicenseAndCheck', [arg0]);
  },
  
  //启用活体检测----返回值可能要等很久
  startLivenessActivity(arg0, success, error){
    exec(success, error, 'CordovaAdvanceAILiveness', 'startLivenessActivity', [arg0]);
  },

  //调整动作顺序----按需修改检测动作顺序
  setActionSequence(arg0, success, error){
    exec(success, error, 'CordovaAdvanceAILiveness', 'setActionSequence', [arg0]);
  },

  //绑定用户ID
  bindUser(arg0, success, error){
    exec(success, error, 'CordovaAdvanceAILiveness', 'bindUser', [arg0]);
  },
  
}

module.exports = CordovaAdvanceAILiveness;