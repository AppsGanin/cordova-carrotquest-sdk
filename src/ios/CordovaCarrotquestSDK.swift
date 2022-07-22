import CarrotSDK

@objc(CordovaCarrotquestSDK)
class CordovaCarrotquestSDK: CDVPlugin {
    
    @objc(setup:)
    func setup(command: CDVInvokedUrlCommand) {
        let apiKey = command.argument(at: 0) as! String
        Carrot.shared.setup(withApiKey: apiKey,
            successHandler: {
                let result = CDVPluginResult(status: CDVCommandStatus_OK)
                self.commandDelegate.send(result, callbackId: command.callbackId)
            },
            errorHandler: { error in
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error);
                self.commandDelegate.send(result, callbackId: command.callbackId)
            })
    }

    @objc(deInit:)
    func deInit(command: CDVInvokedUrlCommand) {
        Carrot.shared.logout(
            successHandler: {
                let result = CDVPluginResult(status: CDVCommandStatus_OK)
                self.commandDelegate.send(result, callbackId: command.callbackId)
            },
            errorHandler: { error in
                let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error);
                self.commandDelegate.send(result, callbackId: command.callbackId)
            })
    }

    @objc(auth:)
    func auth(command: CDVInvokedUrlCommand) {
        let userId = command.argument(at: 0) as! String
        let userAuthKey = command.argument(at: 1) as! String
        Carrot.shared.auth(
            withUserId: userId,
            withUserAuthKey: userAuthKey,
                successHandler: {
                    let result = CDVPluginResult(status: CDVCommandStatus_OK)
                    self.commandDelegate.send(result, callbackId: command.callbackId)
                },
                errorHandler: { error in
                    let result = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error);
                    self.commandDelegate.send(result, callbackId: command.callbackId)
                })
    }

    @objc(openChat:)
    func openChat(command: CDVInvokedUrlCommand) {
        Carrot.shared.openChat()
        let result = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    @objc(setProperties:)
    func setProperties(command: CDVInvokedUrlCommand) {
        let properties = command.argument(at: 0) as! Array<Dictionary<String,String>>
        let userProperties: [UserProperty] = properties.map {
            dict in
            let key = Array(dict.keys)[0]
            return UserProperty(key: key, value: dict[key]!)
        }
        Carrot.shared.setUserProperty(userProperties)
        let result = CDVPluginResult(status: CDVCommandStatus_OK)
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }

    @objc(getUnreadConversations:)
    func getUnreadConversations(command: CDVInvokedUrlCommand) {
        Carrot.shared.getUnreadConversationsCount({ count in
            let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: count);
            self.commandDelegate.send(result, callbackId: command.callbackId)
        })
    }

}
