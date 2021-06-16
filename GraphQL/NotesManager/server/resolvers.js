var _ = require("underscore");
const { UserInputError } = require("apollo-server-errors");
const fetch = require("node-fetch");
// const userService = require("./service/UserService");
// const noteService = require("./service/NotesService");
// const users = userService.getAll();
// console.log(users);
const Query = {
  user: (parent, args, context, info) => {
    const { id } = args;
    return fetch(`http://localhost:7021/users/`).then((res) =>
      res.json().then((data) => _.where(data, { userId: parseInt(id) }))
    );
  },
  users: () => {
    return fetch(`http://localhost:7021/users/`).then((res) => res.json());
  },
};

const User = {
  notes: (User) => {
    return fetch(`http://localhost:7021/users/${User.userId}/notes/`).then(
      (res) => res.json()
    );
  },
};
module.exports = { Query, User };
