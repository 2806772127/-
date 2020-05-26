

(function() {
  "use strict";
  var $, Guillotine, canTransform, defaults, events, getCursorPosition, hardwareAccelerate, isPointerEventCompatible, isTouch, pluginName, scope, validEvent, whitelist,
    __indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; },
    __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };

  $ = jQuery;

  pluginName = 'guillotine';

  scope = 'guillotine';

  events = {
    start: "touchstart." + scope + " mousedown." + scope + " MSPointerDown." + scope,
    move: "touchmove." + scope + "  mousemove." + scope + " MSPointerMove." + scope,
    stop: "touchend." + scope + "   mouseup." + scope + "   MSPointerUp." + scope
  };

  defaults = {
    width: 400,
    height: 300,
    zoomStep: 0.1,
    init: null,
    eventOnChange: null,
    onChange: null
  };

  isTouch = function(e) {
    return e.type.search('touch') > -1;
  };

  isPointerEventCompatible = function() {
    return __indexOf.call(window, 'MSPointerEvent') >= 0;
  };

  validEvent = function(e) {
    if (isPointerEventCompatible() || !isTouch(e)) {
      validEvent = function(e) {
        return (e.which != null) && e.which === 1;
      };
    } else {
      validEvent = function() {
        var _ref, _ref1;
        return ((_ref = e.originalEvent) != null ? (_ref1 = _ref.touches) != null ? _ref1.length : void 0 : void 0) === 1;
      };
    }
    return validEvent(e);
  };

  getCursorPosition = function(e) {
    if (isPointerEventCompatible() || !isTouch(e)) {
      getCursorPosition = function(e) {
        return {
          x: e.pageX,
          y: e.pageY
        };
      };
    } else {
      getCursorPosition = function(e) {
        e = e.originalEvent.touches[0];
        return {
          x: e.pageX,
          y: e.pageY
        };
      };
    }
    return getCursorPosition(e);
  };

  canTransform = function() {
    var hasTransform, helper, prefix, prefixes, prop, test, tests, value, _i, _len;
    hasTransform = false;
    prefixes = 'webkit,Moz,O,ms,Khtml'.split(',');
    tests = {
      transform: 'transform'
    };
    for (_i = 0, _len = prefixes.length; _i < _len; _i++) {
      prefix = prefixes[_i];
      tests[prefix + 'Transform'] = "-" + (prefix.toLowerCase()) + "-transform";
    }
    helper = document.createElement('img');
    document.body.insertBefore(helper, null);
    for (test in tests) {
      prop = tests[test];
      if (helper.style[test] === void 0) {
        continue;
      }
      helper.style[test] = 'rotate(90deg)';
      value = window.getComputedStyle(helper).getPropertyValue(prop);
      if ((value != null) && value.length && value !== 'none') {
        hasTransform = true;
        break;
      }
    }
    document.body.removeChild(helper);
    canTransform = hasTransform ? (function() {
      return true;
    }) : (function() {
      return false;
    });
    return canTransform();
  };

  hardwareAccelerate = function(el) {
    return $(el).css({
      '-webkit-perspective': 1000,
      'perspective': 1000,
      '-webkit-backface-visibility': 'hidden',
      'backface-visibility': 'hidden'
    });
  };

  Guillotine = (function() {
    function Guillotine(element, options) {
      this._drag = __bind(this._drag, this);
      this._unbind = __bind(this._unbind, this);
      this._start = __bind(this._start, this);
      var _ref;
      this.op = $.extend(true, {}, defaults, options, $(element).data(pluginName));
      this.enabled = true;
      this.zoomInFactor = 1 + this.op.zoomStep;
      this.zoomOutFactor = 1 / this.zoomInFactor;
      _ref = [0, 0, 0, 0, 0], this.width = _ref[0], this.height = _ref[1], this.left = _ref[2], this.top = _ref[3], this.angle = _ref[4];
      this.data = {
        scale: 1,
        angle: 0,
        x: 0,
        y: 0,
        w: this.op.width,
        h: this.op.height
      };
      this._wrap(element);
      if (this.op.init != null) {
        this._init();
      }
      if (this.width < 1 || this.height < 1) {
        this._fit() && this._center();
      }
      hardwareAccelerate(this.$el);
      this.$el.on(events.start, this._start);
    }

    Guillotine.prototype._wrap = function(element) {
      var canvas, el, guillotine, height, img, paddingTop, width, _ref, _ref1, _ref2;
      el = $(element);
      if (el.prop('tagName') === 'IMG') {
        img = document.createElement('img');
        img.src = el.attr('src');
        _ref = [img.width, img.height], width = _ref[0], height = _ref[1];
      } else {
        _ref1 = [el.width(), el.height()], width = _ref1[0], height = _ref1[1];
      }
      _ref2 = [width / this.op.width, height / this.op.height], this.width = _ref2[0], this.height = _ref2[1];
      canvas = $('<div>').addClass('guillotine-canvas');
      canvas.css({
        width: this.width * 100 + '%',
        height: this.height * 100 + '%',
        top: 0,
        left: 0
      });
      canvas = el.wrap(canvas).parent();
      paddingTop = this.op.height / this.op.width * 100 + '%';
      guillotine = $('<div>').addClass('guillotine-window');
      guillotine.css({
        width: '100%',
        height: 'auto',
        'padding-top': paddingTop
      });
      guillotine = canvas.wrap(guillotine).parent();
      this.$el = el;
      this.el = el[0];
      this.$canvas = canvas;
      this.canvas = canvas[0];
      this.$gllt = guillotine;
      this.gllt = guillotine[0];
      return this.$document = $(element.ownerDocument);
    };

    Guillotine.prototype._unwrap = function() {
      this.$el.removeAttr('style');
      this.$el.insertBefore(this.gllt);
      return this.$gllt.remove();
    };

    Guillotine.prototype._init = function() {
      var angle, o, scale;
      o = this.op.init;
      if ((angle = parseInt(o.angle))) {
        this._rotate(angle);
      }
      if ((scale = parseFloat(o.scale))) {
        this._zoom(scale);
      }
      return this._offset(parseInt(o.x) / this.op.width, parseInt(o.y) / this.op.height);
    };

    Guillotine.prototype._start = function(e) {
      if (!(this.enabled && validEvent(e))) {
        return;
      }
      e.preventDefault();
      e.stopImmediatePropagation();
      this.p = getCursorPosition(e);
      return this._bind();
    };

    Guillotine.prototype._bind = function() {
      this.$document.on(events.move, this._drag);
      return this.$document.on(events.stop, this._unbind);
    };

    Guillotine.prototype._unbind = function(e) {
      this.$document.off(events.move, this._drag);
      this.$document.off(events.stop, this._unbind);
      if (e != null) {
        return this._trigger('drag');
      }
    };

    Guillotine.prototype._trigger = function(action) {
      if (this.op.eventOnChange != null) {
        this.$el.trigger(this.op.eventOnChange, [this.data, action]);
      }
      if (typeof this.op.onChange === 'function') {
        return this.op.onChange.call(this.el, this.data, action);
      }
    };

    Guillotine.prototype._drag = function(e) {
      var dx, dy, left, p, top;
      e.preventDefault();
      e.stopImmediatePropagation();
      p = getCursorPosition(e);
      dx = p.x - this.p.x;
      dy = p.y - this.p.y;
      this.p = p;
      left = dx === 0 ? null : this.left - dx / this.gllt.clientWidth;
      top = dy === 0 ? null : this.top - dy / this.gllt.clientHeight;
      return this._offset(left, top);
    };

    Guillotine.prototype._offset = function(left, top) {
      if (left || left === 0) {
        if (left < 0) {
          left = 0;
        }
        if (left > this.width - 1) {
          left = this.width - 1;
        }
        this.canvas.style.left = (-left * 100).toFixed(2) + '%';
        this.left = left;
        this.data.x = Math.round(left * this.op.width);
      }
      if (top || top === 0) {
        if (top < 0) {
          top = 0;
        }
        if (top > this.height - 1) {
          top = this.height - 1;
        }
        this.canvas.style.top = (-top * 100).toFixed(2) + '%';
        this.top = top;
        return this.data.y = Math.round(top * this.op.height);
      }
    };

    Guillotine.prototype._zoom = function(factor) {
      var h, left, top, w, _ref;
      if (factor <= 0 || factor === 1) {
        return;
      }
      _ref = [this.width, this.height], w = _ref[0], h = _ref[1];
      if (w * factor > 1 && h * factor > 1) {
        this.width *= factor;
        this.height *= factor;
        this.canvas.style.width = (this.width * 100).toFixed(2) + '%';
        this.canvas.style.height = (this.height * 100).toFixed(2) + '%';
        this.data.scale *= factor;
      } else {
        this._fit();
        factor = this.width / w;
      }
      left = (this.left + 0.5) * factor - 0.5;
      top = (this.top + 0.5) * factor - 0.5;
      return this._offset(left, top);
    };

    Guillotine.prototype._fit = function() {
      var prevWidth, relativeRatio;
      prevWidth = this.width;
      relativeRatio = this.height / this.width;
      if (relativeRatio > 1) {
        this.width = 1;
        this.height = relativeRatio;
      } else {
        this.width = 1 / relativeRatio;
        this.height = 1;
      }
      this.canvas.style.width = (this.width * 100).toFixed(2) + '%';
      this.canvas.style.height = (this.height * 100).toFixed(2) + '%';
      return this.data.scale *= this.width / prevWidth;
    };

    Guillotine.prototype._center = function() {
      return this._offset((this.width - 1) / 2, (this.height - 1) / 2);
    };

    Guillotine.prototype._rotate = function(angle) {
      var canvasRatio, glltRatio, h, w, _ref, _ref1, _ref2;
      if (!canTransform()) {
        return;
      }
      if (!(angle !== 0 && angle % 90 === 0)) {
        return;
      }
      this.angle = (this.angle + angle) % 360;
      if (this.angle < 0) {
        this.angle = 360 + this.angle;
      }
      if (angle % 180 !== 0) {
        glltRatio = this.op.height / this.op.width;
        _ref = [this.height * glltRatio, this.width / glltRatio], this.width = _ref[0], this.height = _ref[1];
        if (this.width >= 1 && this.height >= 1) {
          this.canvas.style.width = this.width * 100 + '%';
          this.canvas.style.height = this.height * 100 + '%';
        } else {
          this._fit();
        }
      }
      _ref1 = [1, 1], w = _ref1[0], h = _ref1[1];
      if (this.angle % 180 !== 0) {
        canvasRatio = this.height / this.width * glltRatio;
        _ref2 = [canvasRatio, 1 / canvasRatio], w = _ref2[0], h = _ref2[1];
      }
      this.el.style.width = w * 100 + '%';
      this.el.style.height = h * 100 + '%';
      this.el.style.left = (1 - w) / 2 * 100 + '%';
      this.el.style.top = (1 - h) / 2 * 100 + '%';
      this.$el.css({
        transform: "rotate(" + this.angle + "deg)"
      });
      this._center();
      return this.data.angle = this.angle;
    };

    Guillotine.prototype.rotateLeft = function() {
      return this.enabled && (this._rotate(-90), this._trigger('rotateLeft'));
    };

    Guillotine.prototype.rotateRight = function() {
      return this.enabled && (this._rotate(90), this._trigger('rotateRight'));
    };

    Guillotine.prototype.center = function() {
      return this.enabled && (this._center(), this._trigger('center'));
    };

    Guillotine.prototype.fit = function() {
      return this.enabled && (this._fit(), this._center(), this._trigger('fit'));
    };

    Guillotine.prototype.zoomIn = function() {
      return this.enabled && (this._zoom(this.zoomInFactor), this._trigger('zoomIn'));
    };

    Guillotine.prototype.zoomOut = function() {
      return this.enabled && (this._zoom(this.zoomOutFactor), this._trigger('zoomOut'));
    };

    Guillotine.prototype.getData = function() {
      return this.data;
    };

    Guillotine.prototype.enable = function() {
      return this.enabled = true;
    };

    Guillotine.prototype.disable = function() {
      return this.enabled = false;
    };

    Guillotine.prototype.remove = function() {
      this._unbind();
      this._unwrap();
      this.disable();
      this.$el.off(events.start, this._start);
      return this.$el.removeData(pluginName + 'Instance');
    };

    return Guillotine;

  })();

  whitelist = ['rotateLeft', 'rotateRight', 'center', 'fit', 'zoomIn', 'zoomOut', 'instance', 'getData', 'enable', 'disable', 'remove'];

  $.fn[pluginName] = function(options) {
    if (typeof options !== 'string') {
      return this.each(function() {
        var guillotine;
        if (!$.data(this, pluginName + 'Instance')) {
          guillotine = new Guillotine(this, options);
          return $.data(this, pluginName + 'Instance', guillotine);
        }
      });
    } else if (__indexOf.call(whitelist, options) >= 0) {
      if (options === 'instance') {
        return $.data(this[0], pluginName + 'Instance');
      }
      if (options === 'getData') {
        return $.data(this[0], pluginName + 'Instance')[options]();
      }
      return this.each(function() {
        var guillotine;
        guillotine = $.data(this, pluginName + 'Instance');
        if (guillotine) {
          return guillotine[options]();
        }
      });
    }
  };

}).call(this);
