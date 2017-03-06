function restore(){
  $("#record, #live").removeClass("disabled");
  $("#pause").replaceWith('<a class="button one" id="pause">Pause</a>');
  $(".one").addClass("disabled");
  Fr.voice.stop();
}

function recordWav(){
    elem = $(this);
    Fr.voice.record($("#live").is(":checked"), function(){
      elem.addClass("disabled");
      $("#live").addClass("disabled");
      $(".one").removeClass("disabled");
    });
  }

function pause(){
    if($(this).hasClass("resume")){
      Fr.voice.resume();
      $(this).replaceWith('<a class="button one" id="pause">Pause</a>');
    }else{
      Fr.voice.pause();
      $(this).replaceWith('<a class="button one resume" id="pause">Resume</a>');
    }
  }

function incrementTime()
{
switchMicrophoneOff();

  Fr.voice.pause();
  if ($("#username").val()=="")
	  uploadWav();
  else
	  uploadNewUserWav();
}

function switchMicrophoneOn()
{
	$("#recordImg").removeClass("recordoff");
	$("#recordImg").addClass("recording");
}

function switchMicrophoneOff()
{
	$("#recordImg").removeClass("recording");
	$("#recordImg").addClass("recordoff");
}

function getvoice()
{
	$("#username").val("");
	recordWav();
}

function getvoicenewuser()
{
   recordWav();
}

function uploadWav(){
    Fr.voice.export(function(blob){
      var formData = new FormData();
      formData.append('file', blob);
      formData.append('username', $("#userList").val());
  
      $.ajax({
        url: "http://localhost:8080/recognito/rest/upload",
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function(output) {        	
        	console.log(output.processed);
        	$("#output").html(output.processed)
        	if (output.processed=='OK')
        		$("#validationMatch").attr("src", "img/smileok.png");
        	else
        		$("#validationMatch").attr("src", "img/smileko.png");
        }
      });
    }, "blob");
    restore();
  }

function uploadNewUserWav(){
	Fr.voice.export(function(blob){
	      var formData = new FormData();
	      formData.append('file', blob);
	      formData.append('username', $("#username").val());
	  
	      $.ajax({
	        url: "http://localhost:8080/recognito/rest/uploadnewuser",
	        type: 'POST',
	        data: formData,
	        contentType: false,
	        processData: false,
	        success: function(output) {        	
	        	console.log(output.processed);
	        	$("#output").html(output.processed);
	        	getUserList();
	        }
	      });
	    }, "blob");
	    restore();
	  }

function getUserList(){
	$.ajax({
        url: "http://localhost:8080/recognito/rest/getuserlist",
        type: 'GET',
        contentType: false,
        processData: false,
        success: function(output) {        	
        	console.log(output.processed);
        	$("#userList").html(output.processed)
        }
      });
}
	
$(document).ready(function(){
	$(document).on("click", "#getvoice", getvoice);
	$(document).on("click", "#getvoicenewuser", getvoicenewuser);
	getUserList();
/*	
  $(document).on("click", "#record:not(.disabled)", recordWav);
  
  $(document).on("click", "#pause:not(.disabled)", pause);
  
  $(document).on("click", "#stop:not(.disabled)", function(){
    restore();
  });
  
  $(document).on("click", "#play:not(.disabled)", function(){
    Fr.voice.export(function(url){
      $("#audio").attr("src", url);
      $("#audio")[0].play();
    }, "URL");
    restore();
  });
  
  $(document).on("click", "#download:not(.disabled)", function(){
    Fr.voice.export(function(url){
      $("<a href='"+url+"' download='MyRecording.wav'></a>")[0].click();
    }, "URL");
    restore();
  });
  
  $(document).on("click", "#base64:not(.disabled)", function(){
    Fr.voice.export(function(url){
      console.log("Here is the base64 URL : " + url);
      alert("Check the web console for the URL");
      
      $("<a href='"+ url +"' target='_blank'></a>")[0].click();
    }, "base64");
    restore();
  });
  
  $(document).on("click", "#mp3:not(.disabled)", function(){
    alert("The conversion to MP3 will take some time (even 10 minutes), so please wait....");
    Fr.voice.export(function(url){
      console.log("Here is the MP3 URL : " + url);
      alert("Check the web console for the URL");
      
      $("<a href='"+ url +"' target='_blank'></a>")[0].click();
    }, "mp3");
    restore();
  });
  
  $(document).on("click", "#save:not(.disabled)", uploadWav);
  */
});
