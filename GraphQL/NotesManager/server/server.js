const fs = require("fs");
const { ApolloServer, gql, ApolloError } = require("apollo-server");
const { v4 } = require("uuid");
let NoteNotFound = require("./errors/NoteNotFound");
const typeDefs = gql(
  fs.readFileSync("./schema.graphql", { encoding: "utf-8" })
);

const resolvers = require("./resolvers.js");

const apolloServer = new ApolloServer({
  typeDefs,
  resolvers,
  formatError: (err) => {
    if (err.message.includes("ECONNREFUSED")) {
      // return new Error("Service that handle this request is down");
      const uuid = v4();
      console.log("Error ID: " + uuid);
      console.log(err);
      return new ApolloError(
        "Service that handle this request is down: " + uuid,
        "SERVICE_DOWN",
        err.stack
      );

      // return new NoteNotFound("Service that handle request is down");
    }
    return err;
  },
});
apolloServer
  .listen({ port: 9000 })
  .then((serverInfo) => console.log(`Server running at ${serverInfo.url}`));
