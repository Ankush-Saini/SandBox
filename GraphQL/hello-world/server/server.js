const { gql, ApolloServer } = require("apollo-server");

//this define interface of our api
const typeDefs = gql`
  type Query {
    greeting: String
  }
`;

//this defines implementation of our api
const resolvers = {
  Query: {
    greeting: () => "Hello GraphQL World!!!",
  },
};

//Creating Server
const server = new ApolloServer({ typeDefs, resolvers });

//Starting server
server
  .listen({ port: 9000 })
  .then((serverInfo) => console.log(`Server running at ${serverInfo.url}`));
