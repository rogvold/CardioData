CardioSpectrum = function(intervals){
    var self = this;
    this.series = intervals;
    this.base = "http://data.cardiomood.com";
    
    this.getSpectrum = function(intervals){
        var d = {
            data: {
                series: intervals,
                method: 'spectrum'
            }
        }

        $.ajax({
            url: self.base + '/CardioDataWeb/resources/calc/getSpectrum',
            //            data: JSON.stringify(d),
            //            data: {
            //                data : d
            //            },
            //            data: JSON.stringify(d),
            data: {
                data: JSON.stringify({
                    series: intervals,
                    method: 'spectrum'
                })
                
            },
            type: 'POST',
            success:function(data){
                console.log(data);
                //                alert(data);
                var sp = data.data;
                self.makeSpectrumTable();
                sp = self.cutSpectrum(sp, 0.5);
                self.drawSpectrum(sp);
            }
        });
    }
    
    this.drawSpectrum = function(sp){
        self.drawPlot('spectrumPlot', sp);
    }

    this.makeSpectrumTable = function(sp){
        var s = '<table>';
        for (var i in sp){
            s+='<tr><td>' + sp[i][0] +'</td><td>' + sp[i][1] +'</td></tr>';
        }
        s+='</table>';
        console.log(s);
        $('#spectrumDataBlock').html(s);
    }
    
    this.cutSpectrum = function(sp, maxFreq){
        var arr = new Array();
        for (var i in sp){
            if (sp[i][0] < maxFreq){
                console.log('adding ' + sp[i][0]);
                arr.push([sp[i][0], sp[i][1]]);
            }
        }
        return arr;
    }
    
    this.drawPlot = function(divId, intervals){
        var options = {
        //            xaxis: {
        //                mode: "time"
        //            }
        }
        $.plot('#' + divId, [intervals], options);
    }
    
}