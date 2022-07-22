const exec = require('cordova/exec');

const carrotQuestNativeBridge = (method, params = []) => {
    return new Promise((resolve, reject) => {
        exec(resolve, reject, 'CordovaCarrotquestSDK', method, params);
    });
}

exports.setup = (apiKey, appId, debug) => {
    return carrotQuestNativeBridge('setup', [apiKey, appId, debug]);
};

exports.deInit = () => {
    return carrotQuestNativeBridge('deInit');
};

exports.auth = (userId, userAuthKey) => {
    return carrotQuestNativeBridge('auth', [userId, userAuthKey]);
};

exports.openChat = () => {
    return carrotQuestNativeBridge('openChat');
};

exports.setProperties = (properties) => {
    return carrotQuestNativeBridge('setProperties', [properties]);
};

exports.getUnreadConversations = () => {
    return carrotQuestNativeBridge('getUnreadConversations');
};

exports.getUnreadConversationsCallback = (callback, errorCallback) => {
    exec(callback, errorCallback, 'CordovaCarrotquestSDK', 'getUnreadConversationsCallback');
}
