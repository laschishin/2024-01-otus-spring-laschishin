var bookList = {};

function fillBookList() {

    var tableContainer = document.querySelector("#books-table tbody");
    queryJson("/api/v1/books")
        .then( (result) => {

            bookList = result;

            result.forEach((book) => {

                tableContainer.innerHTML += `
                <tr>
                    <td>${book.id}</td>
                    <td>
                        <a href="/book/view/${book.id}" >${book.title}</a>
                    </td>
                    <td>${getAuthorsFullNames(book.authors)}</td>
                    <td>${book.genre.name}</td>
                    <td>
                        <a href="/book/edit/${book.id}">Edit</a>
                    </td>
                    <td>
                        <a href="/book/delete/${book.id}">Delete</a>
                    </td>
                </tr>`;
            });
            document.getElementById("error_box").hidden = true;
        })
        .catch ((error) => {
            console.error(error.message);
            document.getElementById("error_box").textContent = `Books fetch error: ${error.message}`
            document.getElementById("error_box").hidden = false;
        });
}