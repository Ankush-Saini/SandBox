var request = require("request");

function getAll() {
  var options = {
    method: "GET",
    url: "http://localhost:7021/users/21/notes/",
    headers: {
      "Content-Type": "application/json",
    },
    body: "",
  };
  request(options, function (error, response) {
    if (error) throw new Error(error);
    return response.body;
  });
}

module.exports = {
  getAll,
};
