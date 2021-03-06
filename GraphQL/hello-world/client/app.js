const rootUrl = "http://localhost:9000/";
async function fetchGreeting() {
  const response = await fetch(rootUrl, {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify({
      query: `
            query{
                greeting
            }
        `,
    }),
  });
  const { data } = await response.json();
  return data;
}

fetchGreeting().then(({ greeting }) => {
  const titlte = document.querySelector("h1");
  titlte.textContent = greeting;
});
