import React from "react";
import { Link } from "react-router-dom";

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userData: [],
      isLoaded: false,
    };
  }
  loadUsers() {
    const rootUrl = "http://localhost:9000/";
    fetch(rootUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ query: this.getUsersQuery() }),
    })
      .then((res) => res.json())
      .then((data) =>
        this.setState({
          userData: Object.values({ data }.data.data.users),
          isLoaded: true,
        })
      );
  }
  getUsersQuery() {
    return `query{
        users{
          userId
          userName
          notes{
              noteId
          }
        }
      }`;
  }
  componentDidMount() {
    this.loadUsers();
  }
  render() {
    if (!this.state.isLoaded) {
      return <div>Loading Users...</div>;
    }
    return (
      <div>
        <ul class="user-list">
          {this.state.userData.map((user) => {
            return (
              <li key={user.userID} class="banner">
                <Link
                  to={{ pathname: "/users/" + user.userId }}
                  class="banner-link"
                >
                  {user.userName}
                  <span>{user.notes.length}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </div>
    );
  }
}
export default Users;
