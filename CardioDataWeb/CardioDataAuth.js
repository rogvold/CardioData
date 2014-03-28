CardioAuth = function(){
    var self = this;
    this.userId = undefined;
    this.userName = undefined;
    this.userEmail = undefined;
    this.userPassword = undefined;
    this.clientServerId = 51;
    this.base = 'http://data.cardiomood.com/resources/';
    this.token = undefined;
    
    this.init = function(){
        self.loadUserDataFromCookies();
    }
    
    this.loadUserDataFromCookies = function(){
        self.userEmail = $.cookie('email');
        self.userPassword = $.cookie('password');
        $('#loginEmail').val(self.userEmail);
        $('#loginPassword').val(self.userPassword);
        self.token = $.cookie('token');
    }
    
    
    
    this.initAuthForm = function(){
        $('#loginForm').bind('click', function(){
            $.ajax({
                url: self.base + 'auth',
                success: function(data){
                    console.log(data);
                }
            });
        });
    }
    
}