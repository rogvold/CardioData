/**
 * Created by sabir on 13.06.14.
 */

CardioMoodSpectrum = function(){
    var self = this;
    this.email = undefined;
    this.token = undefined;
    this.userId = undefined;
    this.sessionId = undefined;
    this.intervals = [];
    this.base = "http://data.cardiomood.com/CardioDataWeb/resources";
    this.creationTimestamp = undefined;
    this.endTimestamp = undefined;
    this.calculatedParametersMap = undefined;
    this.lastModificationTimestamp = undefined;
    this.name = undefined;

    this.init = function(){
        self.sessionId = gup('sessionId');
        console.log('esssionId = ' + sessionId);
    }

    this.loadParameters = function(callback){
        $.ajax({
            url: self.base + "/calc/getCalculatedSession",
            type: 'POST',
            data: {
                sessionId: self.sessionId
            },
            success: function(data){
                if (data.error != undefined){
                    alert(data.error.message);
                    return;
                }
                self.creationTimestamp = data.data.creationTimestamp;
                self.endTimestamp = data.data.endTimestamp;
                self.userId = data.data.userId;
                self.calculatedParametersMap = data.data.calculatedParameters;
                self.name = data.data.name;
                self.lastModificationTimestamp = self.getLastTimestamp();
                self.drawSession();


                if (self.gpsData.fromTimestamp == undefined){
                    self.gpsData.fromTimestamp = self.creationTimestamp;
                }

                self.gpsData.toTimestamp = (self.endTimestamp == undefined) ? self.lastModificationTimestamp : self.endTimestamp;
                self.gpsData.currentBpm = self.getLastBpm();
                self.gpsData.currentSI = self.getLastSI();
                self.gpsData.loadPoints();

                self.sessionUpdatingRequestIsLoaded = true;

            }
        });
    }

}

function gup(name){
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
    var regexS = "[\\?&]"+name+"=([^&#]*)";
    var regex = new RegExp( regexS );
    var results = regex.exec( window.location.href );
    if( results == null )    return "";
    else    return results[1];
}