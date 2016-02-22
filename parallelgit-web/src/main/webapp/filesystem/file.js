app.factory('File', function($q, Connection) {

  function File(parent, attributes) {
    this.name = attributes.name;
    this.parent = parent;
    this.path = resolvePath(parent, attributes.name);
    this.hash = attributes.hash;
    this.type = attributes.type;
    this.state = attributes.state;
  }

  File.prototype.getName = function() {
    return this.name;
  };

  File.prototype.getPath = function() {
    return this.path;
  };

  File.prototype.getParent = function() {
    return this.parent;
  };

  File.prototype.getHash = function() {
    return this.hash;
  };

  File.prototype.unsetHash = function() {
    this.hash = null;
  };

  File.prototype.isDirectory = function() {
    return this.type == 'DIRECTORY';
  };

  File.prototype.loadAttributes = function() {
    var deferred = $q.defer();
    var file = this;
    Connection.send('get-file-attributes', {path: file.getPath()}).then(function(attributes) {
      file.hash = attributes.hash;
      file.type = attributes.type;
      file.state = attributes.state;
      deferred.resolve(attributes);
    });
    return deferred.promise;
  };

  File.prototype.loadChildren = function(refresh) {
    var deferred = $q.defer();
    var dir = this;
    var children = dir.children;
    if(children == null || refresh) {
      Connection.send('list-files', {path: this.path}).then(function(files) {
        children = dir.children = [];
        angular.forEach(files, function(attributes) {
          var node = new File(dir, attributes);
          children.push(node);
        });
        sortFiles(children);
        deferred.resolve(children);
      });
    } else{
      deferred.resolve(children);
    }
    return deferred.promise;
  };

  File.prototype.addChild = function(attributes) {
    var dir = this;
    var children = dir.children;
    var file = new File(dir, attributes);
    children.push(file);
    sortFiles(children);
    return file;
  };

  File.prototype.removeChild = function(file) {
    var dir = this;
    dir.children.splice(dir.children.indexOf(file), 1);
  };

  function resolvePath(parent, name) {
    if(parent != null) {
      var ret = parent.getPath();
      if(ret.charAt(ret.length - 1) != '/')
        ret += '/';
      return ret + name;
    }
    else
      return '/';
  }

  function sortFiles(files) {
    files.sort(function(a, b) {
      if(a.isDirectory() && !b.isDirectory())
        return -1;
      if(!a.isDirectory() && b.isDirectory())
        return 1;
      return a.getName() - b.getName();
    });
  }

  return File;

});