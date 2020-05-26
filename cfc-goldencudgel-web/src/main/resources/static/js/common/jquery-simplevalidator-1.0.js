isSubmit = true;
jQuery.expr[':'].regex = function(elem, index, match) {
	var matchParams = match[3].split(','), validLabels = /^(data|css):/, attr = {
		method : matchParams[0].match(validLabels) ? matchParams[0].split(':')[0] : 'attr',
		property : matchParams.shift().replace(validLabels, '')
	}, regexFlags = 'ig', regex = new RegExp(matchParams.join('').replace(/^\s+|\s+$/g, ''), regexFlags);
	return regex.test(jQuery(elem)[attr.method](attr.property));
};
(function($) {
	var svframework = window['SimpleValidator'] = {};
	svframework.config = {};
	svframework.config.alertMode = 'inline';
	var CF = {
		CLASS_VALID_ERROR : "validerror",
		ATTR_VALID : "isvalid",
		ATTR_BIND : "sv_binded",
		ATTR_MSGBOXID : "sv_msgboxid",
		ERRMSGBOX_CLASS : "sv_errormessage",
		CLOSE_TEXT_CLASS : "sv_close_text"
	}, regexMap = {
		"email" : "^[a-zA-Z0-9]+[a-zA-Z0-9_\\-\\.]*[a-zA-Z0-9]+@[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_\\-]+)+$",
		"date" : "^((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])$",
		"number" : "^(([1-9]\\d*)|(0))(\\.\\d+)?$",
		"integer" : "^\\d+$",
		"letter" : "^[A-Za-z]+$",
		"upperletter" : "^[A-Z]+$",
		"passwd" : "^.*[A-Z]+.*$",
		"lcd" : "^\\d{1,8}(\\.\\d{1,2})?$",
		"useraccount" : "^[a-zA-Z]+[\._]?[a-zA-Z0-9]+$",
		"mobilephone" : "^[1]{1}[0-9]{10}$",
		"idcardbasic" : "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x)|^\\d{3} \\d{3} \\d{6} \\d{3}$|^\\d{3} \\d{3} \\d{8} \\d{3}(\\d|X|x)$)$",
		"zipcode" : "\\d{6}",
		"bigdecimaldata" : "^\\d{1,8}(\\.\\d{1,2})?$",
		"hour" : "^[0-9]+$",
		"minute" : "^[0-9]+$",
		"appnum" : "^[a-zA-Z0-9]{16}$",/* 进件编号 */
		//"applynum" : "^[0-9]{7}$",/* 申请编号 */
		"applynum" : "^(([a-zA-Z][0-9]{8}))$",/* 申请编号 */
		"bankcardnum" : "^[0-9 ]+$",
		"postalcode" : "\\d{6}"
	}, validFuncMap = {
		"date" : function(date) {
			var arr = date.split("/");
			var month = parseInt(arr[1]);
			var nextMothDay = new Date(month == 12 ? parseInt(arr[0]) + 1 : arr[0], month == 12 ? 0 : month, 1);
			return new Date(nextMothDay.getTime() - 24 * 60 * 60 * 1000).getDate() >= parseInt(arr[2]);
		},
		"idcard" : function(cardNo) {
			var cardInfo = getIdCardInfo(cardNo);
			return cardInfo.isTrue;
		},
		"hour" : function(hour) {
			if (parseInt(hour) <= 24) {
				return true;
			}
		},
		"minute" : function(minute) {
			if (parseInt(minute) <= 59) {
				return true;
			}
		}
	}, validatorMap = {}, customerValidatorMap = {}
	// , i18n = {
	// simplevalidator.please.select : "Please select!",
	// simplevalidator.required : "required field!",
	// simplevalidator.fromat.error : "Format error!"
	// }
	, getText = function(key) {
		return i18n ? i18n[key] : key;
	}, isFunc = function(obj) {
		return typeof obj === "function";
	}, isUndefined = function(obj) {
		return typeof obj === "undefined";
	}, isNum = function(n) {
		return !isNaN(parseFloat(n)) && isFinite(n);
	}, nativetypes = {}, nativeattrs = {}, isNativeType = function(type) {
		if (isUndefined(nativetypes[type])) {
			var i = document.createElement("input");
			i.setAttribute("type", type);
			nativetypes[type] = (i.type !== "text");
		}
		return nativetypes[type];
	}, isNativeAttr = function(type, attr) {
		var key = type + "_" + attr;
		if (isUndefined(nativeattrs[key])) {
			nativeattrs[key] = (attr in document.createElement(type));
		}
		return nativeattrs[key];
	}, generateRandomId = function(type, attr) {
		return "rid" + new Date().getTime() + Math.random().toString().substr(2);
	},

	utf8ByteLength = function(str) {
		if (!str)
			return 0;
		var escapedStr = encodeURI(str);
		var match = escapedStr.match(/%/g);
		return match ? (escapedStr.length - match.length * 2) : escapedStr.length;
	}, dbcsByteLength = function(str) {
		if (!str)
			return 0;
		var count = 0;
		for ( var i = 0; i < str.length; i++) {
			count++;
			if (str.charCodeAt(i) >> 8)
				count++;
		}
		return count;
	},
	/*-------------------------------------------------
	getAlertMessageBox
	 */
	getAlertMessageBox = function() {
		var box = $("#sv_alert_message_box");
		if (box.length == 0) {
			var dialogObj = {};
			dialogObj.title = "提示";
			// dialogObj.width = width;
			// dialogObj.height = height;
			dialogObj.modal = true;
			box = $("<div id='sv_alert_message_box'><ol class='conct fsize pad content'></ol><p class='conct pad'><input type='button' value='確定'/></p></div>");
			box.dialog(dialogObj);
			box.find("input:eq(0)").bind("click", function() {
				box.dialog("close");
				box.find("ol").html("");
			});
		}
		return box;
	},
	/*-------------------------------------------------
	alertMessage
	 */
	alertMessage = function(item, message) {
		var box = getAlertMessageBox();
		var lable = item.attr("lable");
		if (isUndefined(lable)) {
			lable = item.attr("name");
			if (isUndefined(lable)) {
				lable = item.attr("id");
			}
		}
		box.find("ol").append("<li>" + lable + ":" + message + "</li>");
		box.dialog();
	},
	/*-------------------------------------------------
	getInlineMessageBox
	 */
	getInlineMessageBox = function(item) {
		item = $(item);
		var boxid = item.attr(CF.ATTR_MSGBOXID);
		var lable = $("#" + boxid);
		if (lable.length <= 0 || !lable.hasClass(CF.ERRMSGBOX_CLASS)) {
			var parent = item.parent();
			var type = item.attr("type");
			var next = item.next();
			var hasMorThanOneSibling = (next.length > 0 && next.next().length > 0);
			lable = $("<lable></lable>").addClass(CF.ERRMSGBOX_CLASS).addClass("nopreview");
			if (hasMorThanOneSibling && type != "checkbox" && type != "radio") {
				if (next.is("img")
						|| (next.is("input") && (next.attr("type") == "button" || next.attr("type") == "image"))) {
					lable.insertAfter(next);
				} else {
					lable.insertAfter(item);
				}
			} else {
				lable.appendTo(parent);
			}
			var rid = generateRandomId();
			lable.attr("id", rid);
			item.attr(CF.ATTR_MSGBOXID, rid);
			if (type != "checkbox" && type != "radio" && hasMorThanOneSibling) {
				var position = item.position();
				// lable.offset({
				// top : position.top,
				// left : position.left
				// });
			}
		}
		return lable;
	},
	/*-------------------------------------------------
	inlineMessage
	 */
	inlineMessage = function(item, message) {
		var msgbox = getInlineMessageBox(item);
		$(msgbox).html(message);
		$("<font>X</font>").addClass(CF.CLOSE_TEXT_CLASS).click(function() {
			msgbox.remove();
		}).appendTo($(msgbox));
	},
	/*-------------------------------------------------
	removeValidError
	 */
	removeValidError = function(item) {
		item = $(item);
		$(item).removeAttr(CF.ATTR_VALID);
		$(item).removeClass(CF.CLASS_VALID_ERROR);

		if (svframework.config.alertMode == 'inline') {
			var boxid = item.attr(CF.ATTR_MSGBOXID);
			var lable = $("#" + boxid);
			if (lable.length > 0 && lable.is("lable") && lable.hasClass(CF.ERRMSGBOX_CLASS)) {
				lable.remove();
			}
		}
	},
	/*-------------------------------------------------
	addValidError
	 */
	addValidError = function(item, msg) {
		item = $(item);
		item.attr(CF.ATTR_VALID, "false");
		item.addClass(CF.CLASS_VALID_ERROR);
		if (!msg) {
			msg = item.attr("validerrormessage");
			if (!msg) {
				msg = getText('simplevalidator.fromat.error');
			}
		}

		if (svframework.config.alertMode == 'inline') {
			inlineMessage(item, msg);
		} else {
			alertMessage(item, msg);
		}

	},
	/* input Validator Chain */
	inputValidatorChain = [
			/* required validator */
			function(item) {
				// if (isUndefined(item.attr("required")) || (item.is("input")
				// && isNativeAttr("input", "required")) || (item.is("textarea")
				// &&
				// isNativeAttr("textarea",
				// "required"))) {
				if (isSubmit == false || isSubmit == "false" || !item.hasClass("required")) {
					return true;
				} else if (item.hasClass("required") && !isUndefined(item.attr("alternative"))) {// 二择一
					var mult = undefined !== item.attr("mult") ? $(item.attr("mult")) : undefined;
					var other = $(item.attr("alternative")), otherMult = other !== undefined ? $(other).attr("mult") !== undefined ? $($(
							other).attr("mult"))
							: undefined
							: undefined;
					var isNotNull = function(m) {
						if (m !== undefined && $(m).length > 0) {
							var bValid = false;
							$(m).each(function() {
								if ((bValid = $.trim($(this).val()) != "")) {
									return false;
								}
							});
							return bValid;
						}
						return false;
					}, removeError = function(o) {
						if ($(o).length > 1) {
							$(o).each(function() {
								removeValidError(this);
							});
						} else {
							removeValidError(o);
						}
					}, alternative = function(o, a, m) {
						// 当前非空，二择一成立,需判断本位是否多元，多元需在没有non-required属性上添加class="required"
						if ("" !== $.trim($(o).val())) {
							removeError($(o));
							removeError($(a));
							removeError($(m));
							return true;
						} else {// 为空，查找本位其它多元是否为空
							if (isNotNull(m)) {
								return true;
							}
						}
						return false;
					}, addAlternativeMsg = function(o, m) {
						if (m !== undefined && $(m).length > 0) {
							removeError($(m));
							addValidError($(m), "");
							addValidError($(m).last(), "二择一");
						} else {
							removeError($(o));
							addValidError($(o), "二择一");
						}
					};
					if (undefined !== other && other.length > 0) {
						if (alternative(item, other, mult)) {
							if (item.attr("non-required") !== undefined) {
								return true;
							}
						} else {
							if (alternative(other, item, otherMult)) {
								return true;
							} else {
								addAlternativeMsg(item, mult);
								addAlternativeMsg(other, otherMult);
								return false;
							}
						}
					}
				}
				var val = item.val();
				if (val != "") {
					var placeholder = item.attr("placeholder");
					if (!isUndefined(placeholder) && placeholder != "" && val == placeholder
							&& !isNativeAttr("input", "placeholder")) {
						val = "";
					}
				}
				var defaultNullValue = "";
				if (item.is("select") && !isUndefined(item.attr("nullvalue"))) {
					var optionval = item.find("option.nullvalue").val();
					if (optionval) {
						defaultNullValue = optionval;
					}
				} else if (item.attr("type") == "checkbox") {
					var name = item.attr("name");
					var nameRegex = name.replace(/\[\d+\]/g, "\\[\\d+\\]").replace(/\./g, "\\.");
					var firstcheckbox = $("input:regex(name," + nameRegex + "):first");
					if ($("input:regex(name," + nameRegex + "):checked").length > 0) {
						val = "checked";
						$("input:regex(name," + nameRegex + ")").each(function() {
							removeValidError($(this));
						});
						return true;
					} else {
						val = "";
					}
					// find the first one to dispaly the error message
					item = firstcheckbox;
				} else if (item.attr("type") == "radio") {
					var name = item.attr("name");
					if ($("input[name='" + name + "']:checked").length > 0) {
						$("input[name='" + name + "']").each(function() {
							removeValidError($(this));
						});
						return true;
					} else {
						val = "";
					}
					item = $("input[name='" + name + "']:last");
				}

				if (val == "" || $.trim(val) == defaultNullValue) {
					valid = false;
					var nullmessage = item.attr("requiredmessage");

					if (isUndefined(nullmessage)) {
						if (item.is("select")) {
							nullmessage = getText('simplevalidator.please.select');
						} else {
							nullmessage = getText('simplevalidator.required');
						}
					}
					addValidError(item, nullmessage);
					return false;
				}
				return true;
			},
			/* length validator */
			function(item) {
				// if ((item.is("input") && isNativeAttr("input", "maxlength"))
				// || (item.is("textarea") && isNativeAttr("textarea",
				// "maxlength")) || item.val()
				// == "") {
				// return true;
				// }
				var valid = true;
				var maxlength = item.attr("maxlength");
				var minlength = item.attr("minlength");
				if (!isUndefined(maxlength) && maxlength != "" && parseInt(maxlength) > 0) {
					if (item.val().length > parseInt(maxlength)) {
						valid = false;
					}
				}

				if (valid && !isUndefined(minlength) && minlength != "" && parseInt(minlength) > 0) {
					if (item.val().length < parseInt(minlength)) {
						valid = false;
					}
				}
				if (valid) {
					var maxUTF8ByteLength = item.attr("maxUTF8ByteLength");
					if (!isUndefined(maxUTF8ByteLength) && maxUTF8ByteLength != "" && parseInt(maxUTF8ByteLength) > 0) {
						if (utf8ByteLength(item.val()) > parseInt(maxUTF8ByteLength)) {
							valid = false;
						}
					}
				}
				if (valid) {
					var maxDBCSByteLength = item.attr("maxDBCSByteLength");
					if (!isUndefined(maxDBCSByteLength) && maxDBCSByteLength != "" && parseInt(maxDBCSByteLength) > 0) {
						if (dbcsByteLength(item.val()) > parseInt(maxDBCSByteLength)) {
							valid = false;
						}
					}
				}

				if (!valid) {
					addValidError(item, item.attr("lengtherrormessage"));
				}
				return valid;
			},
			/* pattern validator */
			function(item) {
				// if (!item.is("input") || isNativeAttr("input", "pattern")) {
				if (!item.is("input")) {
					return true;
				}

				var pattern = item.attr("_pattern");
				var val = item.val();
				if (!isUndefined(pattern) && pattern != "" && val != "") {
					if (pattern.indexOf("^") != 0 && !pattern.indexOf("$") != pattern.length) {
						pattern = "^" + pattern + "$";
					}
					var regex = new RegExp(pattern, "g");
					if (!regex.test(val)) {
						addValidError(item, item.attr("patternerrormessage"));
						return false;
					}
				}
				return true;
			},
			/* type validator */
			function(item) {
				// if (item.val() == "" || !item.is("input") ||
				// isNativeType(item.attr("type"))) {
				if (item.val() == "" || !item.is("input")) {
					return true;
				}
				var val = item.val();
				if (val != "") {
					var placeholder = item.attr("placeholder");
					if (!isUndefined(placeholder) && placeholder != "" && val == placeholder
							&& !isNativeAttr("input", "placeholder")) {
						val = "";
						return true;
					}
				}

				var type = item.attr("datatype");
				var regex;
				if (type != null) {
					type = type.toLowerCase();
					regex = regexMap[type];
				} else {
					type = item.attr("type")?item.attr("type").toLowerCase():"";
					regex = regexMap[type];
				}
				if (regex != null && regex != "") {
					if (!new RegExp(regex, "g").test(val)) {
						addValidError(item, item.attr("typeerrormessage"));
						return false;
					}
				}
				var validFunc = validFuncMap[type];
				if (validFunc != null && isFunc(validFunc)) {
					if (!validFunc.call(this, val)) {
						addValidError(item, item.attr("typeerrormessage"));
						return false;
					}
				}

				return true;
			},
			/* range validator */
			function(item) {
				// if (!item.is("input") || item.attr("type") != "range" ||
				// isNativeType("range")) {
				if (!item.is("input") ) {
					return true;
				}
				var val = item.val();
				var min = item.attr("min");
				var max = item.attr("max");
				if (val != "" && (!isUndefined(min) || !isUndefined(max))) {
					try {
						if (!isUndefined(min) && (parseFloat(val) < parseFloat(min))
								|| (!isUndefined(max) && parseFloat(val) > parseFloat(max))) {
							addValidError(item, item.attr("rangeerrormessage"));
							return false;
						}
					} catch (e) {
						alert(e);
						addValidError(item, item.attr("formaterrormessage"));
						return false;
					}
				}
				return true;
			},
			/* relation validator */
			function(item) {
				if (!item.is("input")) {
					return true;
				}
				var valid = true;
				var equalTo = item.attr("equalTo");
				if (!isUndefined(equalTo)) {
					var target = $('' + equalTo);
					if (target.length > 0) {
						try {
							if (item.val() != target.val()) {
								valid = false;
							}
						} catch (e) {
							alert(e);
							valid = false;
						}
						if (!valid) {
							var msg = item.attr("equalerrormessage");
							addValidError(item, msg ? msg : "not equal to " + target.attr("name"));
							return false;
						}
					}
				}
				var greatThan = item.attr("greatThan");
				if (!isUndefined(greatThan)) {
					var target = $('' + greatThan);
					if (target.length > 0) {
						try {
							var me = item.val();
							var other = target.val();
							if (isNum(me) && isNum(other) && parseFloat(me) <= parseFloat(other)) {
								valid = false;
							} else if (item.attr("datatype") === "date") {
								var meLong = Date.parse(me);
								var otherLong = Date.parse(other);
								if (meLong <= otherLong) {
									valid = false;
								}
							} else if (me <= other) {
								valid = false;
							}
						} catch (e) {
							alert(e);
							valid = false;
						}
						if (!valid) {
							var msg = item.attr("greatthanerrormessage");
							addValidError(item, msg ? msg : "must great than " + target.val());
							return false;
						}
					}
				}
				return true;
			} ];
	/*-------------------------------------------------
	validateInput
	 */
	function validateInput(input) {
		for ( var i in inputValidatorChain) {
			if (!inputValidatorChain[i].call(this, $(input))) {
				$(input).attr(CF.ATTR_VALID, false);
				return false;
			}
			var id = $(input).attr("id");
			var func = validatorMap[id];
			if (func && !func.call()) {
				$(input).attr(CF.ATTR_VALID, false);
				return false;
			}
			var validator = $(input).attr("validator");
			var custFunc = customerValidatorMap[validator];
			if (custFunc && !custFunc.call($(input))) {
				$(input).attr(CF.ATTR_VALID, false);
				addValidError($(input));
				return false;
			}
		}
		removeValidError(input);
		return true;
	}
	/*-------------------
	triggerValidateInput
	 */
	function triggerValidateInput(item) {
		item = $(item);
		if (isUndefined(item.attr("validateprocessing"))) {
			item.attr("validateprocessing", "true");
			try {
				validateInput(item);
			} catch (e) {
				alert(e);
			}
			/* don't validate again in 200ms */
			setTimeout(function() {
				item.removeAttr("validateprocessing");
			}, 200);
		}
	}
	/*-------------------
	validateForm
	 */
	function validateForm(form, validator) {
		var valid = true;
		form = $(form);
		/* trigger the blur event to validate for the focus element */
		form.find("input:focus,textarea:focus").blur();
		form.find(
				"input[" + CF.ATTR_BIND + "]:not([" + CF.ATTR_VALID + "]),textarea[" + CF.ATTR_BIND + "]:not(["
						+ CF.ATTR_VALID + "]),select[" + CF.ATTR_BIND + "]:not([" + CF.ATTR_VALID + "])").each(
				function() {
					if (!validateInput(this)) {
						valid = false;
					}
				});

		var errorElements = form.find("input[" + CF.ATTR_VALID + "=false],textarea[" + CF.ATTR_VALID
				+ "=false],select[" + CF.ATTR_VALID + "=false]");
		if (errorElements.length > 0) {
			valid = false;
		}

		if (errorElements.length > 0) {
			for ( var i = 0; i < errorElements.length; i++) {
				if (!($(errorElements[i]).is(':hidden'))) {
					errorElements[i].focus();
					break;
				}
			}
		}
		if (valid) {
			valid = (!validator || validator.call());
		}
		if (valid) {
			var id = $(form).attr("id");
			var func = validatorMap[id];
			valid = (!func || func.call());
		}
		return valid;
	}
	/*-------------------
	setSimpleValidator
	 */
	function setSimpleValidator(obj, validator) {
		if (validator) {
			obj = $(obj);
			if (obj.is("input") || obj.is("select") || obj.is("textarea") || obj.is("form")) {
				var id = obj.attr("id");
				if (id == null) {
					id = generateRandomId();
					obj.attr("id", id);
				}
				validatorMap[id] = validator;
			}
		}
	}
	/*-------------------
	isBinded
	 */
	function isBinded(obj) {
		return !isUndefined($(obj).attr(CF.ATTR_BIND));
	}
	function bind(obj, func) {
		if (!isBinded(obj)) {
			$(obj).attr(CF.ATTR_BIND, true);
			func.call();
		}
	}
	function unbind(obj) {
		$(obj).removeAttr(CF.ATTR_BIND);
	}
	function bindInput(input) {
		bind(input, function() {
			$(input).on("blur", function() {
				if (isBinded(this))
					triggerValidateInput(this);
			});
			$(input).on("change", function() {
				if (isBinded(this))
					triggerValidateInput(this);
			});
		});
	}
	function unbindInput(input) {
		unbind(input);
		$(input).unbind("blur").unbind("change");
		removeValidError(input);
	}
	function bindForm(form, validator) {
		bind(form, function() {
			$(form).find("input:not(.novalid[type=button][type=submit]),select:not(.novalid),textarea:not(.novalid)")
					.each(function() {
						bindInput(this);
					});
			$(form).submit(function() {
				return !isBinded(form) || validateForm(form, validator);
			});
		});
	}
	function unbindForm(form) {
		unbind(form);
		$(form).unbind('submit').find(
				"input[" + CF.ATTR_BIND + "],select[" + CF.ATTR_BIND + "],textarea[" + CF.ATTR_BIND + "]").each(
				function() {
					unbindInput(this);
				});
	}

	/* function used to extend SimpleValidator */
	svframework.addType = function(type, regex, validFunc) {
		regexMap[type] = regex;
		validFuncMap[type] = validFunc;
	};
	/* function used to extend SimpleValidator */
	svframework.addCustomerValidator = function(name, validator) {
		customerValidatorMap[name] = validator;
	};
	/* extend jquery */
	$.fn.extend({
		bindSimpleValidator : function(validator) {
			var obj = $(this);
			if (obj.is("input") || obj.is("select") || obj.is("textarea")) {
				bindInput(obj);
			} else if (obj.is("form")) {
				bindForm(obj);
				if (validator) {
					setSimpleValidator(obj);
				}
			}
		},
		unbindSimpleValidator : function() {
			var obj = $(this);
			if (obj.is("input") || obj.is("select") || obj.is("textarea")) {
				unbindInput(obj);
			} else if (obj.is("form")) {
				unbindForm(obj);
			}
		},
		simpleValidate : function() {
			var obj = $(this);
			if (obj.is("input") || obj.is("select") || obj.is("textarea")) {
				return validateInput(obj);
			} else if (obj.is("form")) {
				return validateForm(obj);
			}
		},
		addValidError : function(msg) {
			addValidError($(this), msg);
		},
		removeValidError : function() {
			removeValidError($(this));
		},
		setValidator : function(validator) {
			setSimpleValidator(validator);
		}
	});
})(jQuery);

function getIdCardInfo(cardNo) {
	var info = {
		isTrue : false,
		year : null,
		month : null,
		day : null,
		isMale : false,
		isFemale : false
	};
	if (!cardNo && 15 != cardNo.length && 18 != cardNo.length) {
		info.isTrue = false;
		return info;
	}
	if (15 == cardNo.length) {
		var year = cardNo.substring(6, 8);
		var month = cardNo.substring(8, 10);
		var day = cardNo.substring(10, 12);
		var p = cardNo.substring(14, 15); // 性别位
		var birthday = new Date(year, parseFloat(month) - 1, parseFloat(day));
		// 对于老身份证中的年龄则不需考虑千年虫问题而使用getYear()方法
		if (birthday.getYear() != parseFloat(year) || birthday.getMonth() != parseFloat(month) - 1
				|| birthday.getDate() != parseFloat(day)) {
			info.isTrue = false;
		} else {
			info.isTrue = true;
			info.year = birthday.getFullYear();
			info.month = birthday.getMonth() + 1;
			info.day = birthday.getDate();
			if (p % 2 == 0) {
				info.isFemale = true;
				info.isMale = false;
			} else {
				info.isFemale = false;
				info.isMale = true
			}
		}
		return info;
	}
	if (18 == cardNo.length) {
		var year = cardNo.substring(6, 10);
		var month = cardNo.substring(10, 12);
		var day = cardNo.substring(12, 14);
		var p = cardNo.substring(14, 17)
		var birthday = new Date(year, parseFloat(month) - 1, parseFloat(day));
		// 这里用getFullYear()获取年份，避免千年虫问题
		if (birthday.getFullYear() != parseFloat(year) || birthday.getMonth() != parseFloat(month) - 1
				|| birthday.getDate() != parseFloat(day)) {
			info.isTrue = false;
			return info;
		}
		var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子
		var Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X
		// 验证校验位
		var sum = 0; // 声明加权求和变量
		var _cardNo = cardNo.split("");
		if (_cardNo[17].toLowerCase() == 'x') {
			_cardNo[17] = 10;// 将最后位为x的验证码替换为10方便后续操作
		}
		for ( var i = 0; i < 17; i++) {
			sum += Wi[i] * _cardNo[i];// 加权求和
		}
		var i = sum % 11;// 得到验证码所位置
		if (_cardNo[17] != Y[i]) {
			return info.isTrue = false;
		}
		info.isTrue = true;
		info.year = birthday.getFullYear();
		info.month = birthday.getMonth() + 1;
		info.day = birthday.getDate();
		if (p % 2 == 0) {
			info.isFemale = true;
			info.isMale = false;
		} else {
			info.isFemale = false;
			info.isMale = true
		}
		return info;
	}
	return info;
}

jQuery(document).ready(function() {
	window['SimpleValidator'].config.alertMode = "inline";
	jQuery("form[class=simplevalidator]").bindSimpleValidator();
	// loadi18n([ "simplevalidator.please.select", "simplevalidator.required", "simplevalidator.fromat.error" ]);
});