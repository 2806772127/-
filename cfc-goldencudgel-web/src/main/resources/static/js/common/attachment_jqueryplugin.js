var localDemoImageFilePath = "c:/comwave_bpm_image.jpg";

/**
 * 启动录像弹出框
 * 
 * @param divId
 *            对话框ID
 */
(function($) {

	if (attachframework) {
		return;
	}
	var attachframework = window.attachframework = {

		/** **************照片查看控件***************** */
		currDisplayImagePath : null,
		captureMode : null, // 拍摄模式:image/video
		hasSaveJPG : false,
		hasStartedVideoCapture : false,
		hasSaveVideo : false,

		showErrorMessge : function(message) {
			$("#error_message_box").text(message);
		},
		getPicture : function() {
			return $("#imageShow");
		},
		/** 加载图片到ActiveX控件里* */
		loadImage : function(fileId) {
			if (fileId == null) {
				return;
			}
			var url = context_path + "/loaninfo/loadAttachment?source=true&fileId=" + fileId; // 获取原始照片

			attachframework.currDisplayImagePath = url;
			var picture = attachframework.getPicture();
			picture.on('load', function() {
				picture.parent().css("width","100%");
				picture.guillotine({
					eventOnChange : null
				});
			});
			picture.attr("src", url);
			picture.guillotine('fit');

		},

		printImage : function() { // 打印
			var attachId = $("#curr_show_attach_id").val();
			window.open(context_path + "/search/printImage?attachId=" + attachId);
		},
		/** 放大* */
		zoomIn : function() {
			attachframework.getPicture().guillotine('zoomIn');
		},

		/** 缩小* */
		zoomOut : function() {
			attachframework.getPicture().guillotine('zoomOut');
		},

		rotateLeft : function() {
			attachframework.getPicture().guillotine('rotateLeft');
		},
		rotateRight : function() {
			attachframework.getPicture().guillotine('rotateRight');
		},
		fixedSize : function() {
			attachframework.getPicture().guillotine('fit');
		},

		changeImages : function(obj) {
			$("#ImageShow_container").show();
			var image = $(obj);
			var url = image.attr("src");
			var attachId = image.attr("attachId");
			$("#curr_show_attach_id").val(attachId); // 记录当前选择的附件编号
			attachframework.loadImage(attachId);
		},

		setImageScroll : function() {
			$(".album .dct").jCarouselLite({
				btnNext : ".album .next",
				btnPrev : ".album .prev",
				circular : false,
				visible : 5
			});
		},

		removeAttachment : function(qualificationRemoveCallBack) {
			var attachId = $("#curr_show_attach_id").val(); // 获得当前显示的附件编号
			if (attachId == "") {
				alertInfo("提示", "尚未选择要删除的附件!");
				return;
			}
			var showImageXObjectContainer = function() {
				$("#ImageShow_container").show();
			}
			$("#ImageShow_container").hide();
			confirmInfo_3({
				title : "删除提示",
				close : showImageXObjectContainer,
				width : 250,
				height : 130,
				modal : true
			}, "確定刪除附件？", function() {
				$.ajax({
					url : context_path + "/loaninfo/removeAttachment",
					type : "POST",
					data : {
						"attachId" : attachId
					},
					dataType : "json",
					success : function(reqData) {
						alertInfo("提示", reqData.message, function() {
							if(qualificationRemoveCallBack == undefined){
								showImageXObjectContainer.call();
								if (reqData.success) {
									var image = $("img[attachId=" + attachId + "]");
									var li = image.parent();
									var ul = li.parent();
									li.remove(); // 删除图像显示

									var nextImage = ul.find("img").first();
									if (nextImage.length > 0) {
										attachframework.changeImages(nextImage);
									} else {
										$("#selectedAttachment").children("span").addClass("red01");// 还没有上传
										ImageXObject.Clear();
									}
								}
							} else {
								if(typeof qualificationRemoveCallBack == 'function'){
									qualificationRemoveCallBack.call(this,reqData.success)
								}
							}
						});
					}
				});
			});
		},
		/** ****************拍照录影******************* */

		makelist : function() {
			var count = VideoCapXObject.GetVideoDeviceCount();
			var node = "";
			$("#combo1").empty();
			for ( var f = 0; f < count; f++) {
				var option = document.createElement("option");
				option.value = f;
				option.innerText = VideoCapXObject.GetVideoDeviceName(f);
				document.getElementById("combo1").appendChild(option);
			}
			document.getElementById("combo1").value = 0;
			attachframework.camclick();
		},
		camclick : function() {
			VideoCapXObject.Connected = false;
			VideoCapXObject.VideoDeviceIndex = document.getElementById("combo1").value;
			VideoCapXObject.Connected = true;
			VideoCapXObject.Preview = true;
			VideoCapXObject.PreviewScale = true;
			// VideoCapXObject.SetVideoFormat(320, 240)// 不设置 默认为2592*1944 接受
			// 320*240 ，640*480 1600*1200 2592*1944
			VideoCapXObject.SetTextOverlay(0, "TIME", 0, 0, "Arial", 10, 255, -1);
		},

		stopCam : function() {
			VideoCapXObject.Connected = false;
		},
		getVideoCodecIndex : function() {
			for ( var f = 0; f < VideoCapXObject.GetVideoCodecCount(); f++) {
				var codecName = VideoCapXObject.GetVideoCodecName(f);
				if (codecName == "Microsoft Video 1") {
					return f;
					break;
				}
			}
			return 0;
		},
		getAudioCodecIndex : function() {
			for ( var f = 0; f < VideoCapXObject.GetAudioCodecCount(); f++) {
				var codecName = VideoCapXObject.GetAudioCodecName(f);
				if (codecName == "Microsoft ADPCM") {
					return f;
					break;
				}
			}
			return 0;
		},
		StartCapture : function() {
			attachframework.cleanErrorMessage();
			attachframework.hasStartedVideoCapture = true;
			/*
			 * Video Codec: #0:WMVideo8 Encoder DMO #1:WMVideo9 Encoder DMO
			 * #2:MSScreen 9 encoder DMO #3:DV Video Encoder #4:MJPEG Compressor
			 * #5:Cinepak Codec by Radius #6:Intel IYUV 编码解码器 #7:Intel IYUV
			 * 编码解码器 #8:Microsoft RLE #9:Microsoft Video 1
			 */
			VideoCapXObject.VideoCodecIndex = attachframework.getVideoCodecIndex();
			/*
			 * Audio Codec: #0:WM Speech Encoder DMO #1:WMAudio Encoder DMO
			 * #2:IMA ADPCM #3:PCM #4:Microsoft ADPCM #5:GSM 6.10 #6:CCITT A-Law
			 * #7:CCITT u-Law #8:MPEG Layer-3
			 */
			VideoCapXObject.AudioCodecIndex = attachframework.getAudioCodecIndex();
			VideoCapXObject.DebugMode = 0;
			VideoCapXObject.CapTimeLimitEnabled = true;
			VideoCapXObject.CapTimeLimit = 30; // 30 seconds
			VideoCapXObject.Preview = true;
			// 视频质量 30-100
			VideoCapXObject.VideoCodecQuality = 70;
			VideoCapXObject.StartCapture(); // 启动录像
		},
		StopCapture : function() {
			if (attachframework.hasStartedVideoCapture) {
				attachframework.hasSaveVideo = true;
				VideoCapXObject.StopCapture();// 结束录像
				attachframework.showErrorMessge("录像完成!");
				$("#_upload_video").prop("disabled", false); // 启用录像上传按钮
			} else {
				attachframework.showErrorMessge("请先点击开始录像!");
			}
		},

		GrabFrame : function() {
			$("#_photo_image").prop("disabled", true); // 禁用拍照按钮
			attachframework.cleanErrorMessage();
			attachframework.hasSaveJPG = true;
			VideoCapXObject.SaveFrameJPG(localDemoImageFilePath, 10);
			attachframework.showErrorMessge("拍照完成!");
			$("#_upload_image").prop("disabled", false); // 启用图片上传按钮
		},
		/** ************************控件操作end*************************************** */

		/** *****************ajax file upload******************* */
		uploadImage : function(qualificationUploadCallBack) {
			if (attachframework.hasSaveJPG) {
				$("#_upload_image").prop("disabled", true); // 禁用图片上传按钮
				attachframework.videoCapXHttpUpload("image",qualificationUploadCallBack);
				// attachframework.showErrorMessge('文件上传中...');
			} else {
				attachframework.showErrorMessge("请先点击拍照后再上传!");
			}
		},
		uploadVideo : function() {
			if (attachframework.hasSaveVideo) {
				$("#_upload_video").prop("disabled", true); // 禁用录像上传按钮
				attachframework.videoCapXHttpUpload("video");
				// attachframework.showErrorMessge('文件上传中...');
			} else {
				attachframework.showErrorMessge("请先录像后再上传!");
			}
		},
		videoCapXHttpUpload : function(type,qualificationUploadCallBack) {
			var result = true;
			try {
				$("#VideoCapXObject_container").hide();
				var host = location.protocol + '//' + location.host;
				var url = context_path + "/loaninfo/activexUpload";
				var appNum = $("#selectedAttachment").attr("appNum");
				var attachTypeCode = $("#selectedAttachment").attr("attachTypeCode");
				var attachTypeName = $("#selectedAttachment").attr("attachTypeName");
				var encryptSession = $("#_encryptSession").val();
				var fields = "appNum|" + appNum + "|attachTypeCode|" + attachTypeCode + "|attachTypeName|" + encodeURIComponent(attachTypeName);
				fields += "|username|" + $("#curr_user_name").val() + "|encryptSession|" + encryptSession;
				var files = "";
				var b = true;
				if (type == "image") {
					files = "imageFile|" + localDemoImageFilePath;
				} else {
					// 检测视频是否在规定的大小内
					files = "imageFile|c:/comwave_bpm_video.avi";
					var fileSize = VideoCapXObject.GetCapFileSize();
					if (fileSize > 20971520) {
						attachframework.hasSaveJPG = false;
						$("#error_message_box").text("上传的文件应该在20M内");
						b = false;
						result = false;
					}
				}
				if (b) {
					// 上传影像
					var resultString = VideoCapXObject.HTTPUpload(host, url, fields, files);
					bodyunmask();
					if (resultString != null && resultString != '') {
						var first2Char = resultString.substr(0, 2);
						if (first2Char == "7e" ||first2Char == "2c" ||first2Char == "3d") {
							resultString = resultString.substr(4, resultString.length - 9);
						}
					}
					result = jQuery.parseJSON(resultString);
					if (result.success) {
						// 清除消息
						attachframework.cleanErrorMessage();
						// 上传成功，标记已上传
						alertInfo("提示", "上传成功!", function() {
							attachframework.closeDialog();
							attachframework.hasSaveJPG = false;
							$("#error_message_box").text("");
							$("#selectedAttachment").children("span").removeClass("red01");// 已经上传
							
							if(qualificationUploadCallBack==undefined){
								refreshLastOpenTab();
					        } else {
					            if(typeof qualificationUploadCallBack == 'function'){
									qualificationUploadCallBack.call(this,result.message);
					            }
					        }
						});
					} else {
						attachframework.showErrorMessge("上传失败,请重试!");
						$("#ImageShow_container").hide();// 隐藏图像显示
						$("#VideoCapXObject_container").show();// 显示视频画面
						result = false;
					}
				} else {
					bodyunmask();
					$("#ImageShow_container").hide();// 隐藏图像显示
					$("#VideoCapXObject_container").show();// 显示视频画面
				}
			} catch (e) {
				bodyunmask();
				$("#ImageShow_container").hide();// 隐藏图像显示
				attachframework.showErrorMessge("上传失败:" + e + result);
				result = false;
			}
			if (result == false) {
				$("#_photo_image").prop("disabled", false);
				$("#_upload_image").prop("disabled", false);
				$("#_upload_video").prop("disabled", false);
			}
		},

		openVideoCaptureDialog : function() {
			$("#ImageShow_container").hide();
			attachframework.stopCam();
			attachframework.cleanErrorMessage();
			$("#_show_photo_note").hide();
			$("#_show_vedio_note").hide();
			$("#_show_photo_btn").hide();
			$("#_show_vedio_btn").hide();
			$("#_show_vedio_note").show();
			$("#_show_vedio_btn").show();
			$("#div_video_photo").dialog({
				title : '启动录像',
				width : 650,
				height : 450,
				modal : true,
				close : function() {
					attachframework.closeDialog();
				}
			});

			$("#VideoCapXObject_container").show();
			attachframework.captureMode = "video";
			attachframework.makelist();
		},

		/** 启动照相弹出框* */
		openImageCaptureDialog : function() {
			$("#ImageShow_container").hide();
			attachframework.stopCam();
			attachframework.cleanErrorMessage();
			$("#_show_photo_note").hide();
			$("#_show_vedio_note").hide();
			$("#_show_photo_btn").hide();
			$("#_show_vedio_btn").hide();
			$("#_show_photo_note").show();
			$("#_show_photo_btn").show();
			$("#div_video_photo").dialog({
				title : '启动拍照',
				width : 650,
				height : 450,
				modal : true,
				close : function() {
					attachframework.closeDialog();
				}
			});
			$("#VideoCapXObject_container").show();
			$("#_photo_image").prop("disabled", false); // 拍照按钮
			$("#_upload_image").prop("disabled", true); // 图片上传按钮
			$("#_upload_video").prop("disabled", true); // 录像上传按钮
			attachframework.captureMode = "image";
			attachframework.makelist();
		},

		closeDialog : function() {
			attachframework.stopCam();
			$('#div_video_photo').dialog("close");
			$("#ImageShow_container").show();
		},

		cleanErrorMessage : function() {
			$("#error_message_box").text("");
		}
	};

})(jQuery);
