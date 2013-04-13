
$(function () {
  var taskModel = function (task) {
    var self = this;

    self.id = ko.observable(task.id);
    self.label = ko.observable(task.label);
  };

  var viewModel = function () {
    var self = this;

    self.newTaskLabel = ko.observable();
    self.tasks = ko.observableArray();
    self.alertMessage = ko.observable();
    self.pageIndex = ko.observable(0);

    self.addTask = function () {
      $.ajax({
        type: 'POST',
        url: '/tasks',
        data: JSON.stringify({
          label: self.newTaskLabel()
        }),
        dataType: 'json',
        contentType: 'application/json',
        success: function (result) {
          self.tasks.push(new taskModel({ id: result.id, label: result.label }));
          self.newTaskLabel('');
          self.alertMessage('Task was added successfully.');
        }
      });
    }

    self.deleteTask = function (task) {
      if (confirm('Are you sure you want to delete this task?')) {
        $.post('/tasks/' + task.id() + '/delete', function () {
          self.tasks.remove(task);
          self.alertMessage('Task was deleted successfully.');
        });
      }
    };

    self.loadTasks = function () {
        var pageSize = 5, pageIndex = self.pageIndex();

        $.get('/tasks/list/' + pageSize + '/' + pageIndex, function (result) {
            var tasks = $.map(result, function (task) {
                return new taskModel(task);
            });

            model.tasks(tasks);
        });
    };

    self.setPage = function (index) {
        self.pageIndex(index);
        self.loadTasks();
    };
  };

  var model = new viewModel();
  ko.applyBindings(model);

  model.loadTasks();
});