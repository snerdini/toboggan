
$(function () {
  var taskModel = function (task) {
    var self = this;

    self.id = ko.observable(task.id);
    self.label = ko.observable(task.label);
  };

  var viewModel = function () {
    var self = this;

    self.totalTasks = ko.observable();
    self.newTaskLabel = ko.observable();
    self.tasks = ko.observableArray();
    self.alertMessage = ko.observable();
    self.pageIndex = ko.observable(0);
    self.pagerLinks = ko.observableArray();

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
          self.loadTasks();
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
          self.totalTasks(self.totalTasks() - 1);
        });
      }
    };

    self.loadTasks = function () {
        var pageSize = 5, pageIndex = self.pageIndex();

        $.get('/tasks/list/' + pageSize + '/' + pageIndex, function (result) {
            self.totalTasks(result.totalRows);
            var pagerCount = result.totalRows / pageSize;
            self.pagerLinks([]);

            for (var i = 0; i < pagerCount; i++) {
                self.pagerLinks.push({});
            }

            var tasks = $.map(result.tasks, function (task, index) {
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