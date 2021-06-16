var request = require("request");

function getAll() {
  var options = {
    method: "GET",
    url: "http://localhost:7021/users/",
    headers: {
      "Content-Type": "application/json",
    },
    body: "",
    json: true,
  };
  let users = [];
  request(options, function (error, response) {
    if (error) throw new Error(error);
    users = response.body;
  });
  return users;
}

module.exports = {
  getAll,
};
