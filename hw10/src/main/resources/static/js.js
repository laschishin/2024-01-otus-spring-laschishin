async function queryJson(url) {

    try {

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        var json_data;

        try{
            json_data = await response.json();
        }
        catch (error) {
            throw new Error("Response has no json data");
        }

        if(json_data === null) {
            throw new Error("Response has no entites");
        }

        return json_data;

    } catch (error) {
        return error;
    }
}

function fillBooksTable() {

    queryJson("/api/v1/books")
        .then( (result) => {

            if(result instanceof Error) {
                throw new Error(result);
            }

            result.forEach((book) => {

                document.querySelector("#books-table tbody").innerHTML += `
                <tr>
                    <td>${book.id}</td>
                    <td>
                        <a href="/book/view/${book.id}" >${book.title}</a>
                    </td>
                    <td>${book.authorsFullNames}</td>
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

//async function fillBooksTable1() {
//
//    const url = "/api/v1/books";
//
//    try {
//
//        const response = await fetch(url);
//
//        if (!response.ok) {
//            throw new Error(`Response status: ${response.status}`);
//        }
//
//        var books;
//
//        try{
//            books = await response.json();
//        }
//        catch (error) {
//            throw new Error("Response has no json data");
//        }
//
//
//        if(books === null) {
//            throw new Error("Response has no entites");
//        }
//
//        books.forEach((book) => {
//
//            document.getElementById("books-table").tBodies[0].innerHTML += `
//    <tr>
//        <td>${book.id}</td>
//        <td>
//            <a href="/book/view/${book.id}" >${book.title}</a>
//        </td>
//        <td>${book.authorsFullNames}</td>
//        <td>${book.genre.name}</td>
//        <td>
//            <a href="/book/edit/${book.id}">Edit</a>
//        </td>
//        <td>
//            <a href="/book/delete/${book.id}">Delete</a>
//        </td>
//    </tr>
//`;
//        });
//        document.getElementById("error_box").hidden = true;
//
//    } catch (error) {
//        console.error(error.message);
//        document.getElementById("error_box").textContent = `Books fetch error: ${error.message}`
//        document.getElementById("error_box").hidden = false;
//    }
//};
//
//async function fillBooksTable2() {
//    const booksTable = document.querySelector("#books-table tbody");
//    const myRequest = new Request("/api/v1/books");
//
//    fetch(myRequest)
//        .then((response) => response.json())
//        .then((data) => {
//        for (const book of data) {
//            booksTable.innerHTML += `
//    <tr>
//        <td>${book.id}</td>
//        <td>
//            <a href="/book/view/${book.id}" >${book.title}</a>
//        </td>
//        <td>${book.authorsFullNames}</td>
//        <td>${book.genre.name}</td>
//        <td>
//            <a href="/book/edit/${book.id}">Edit</a>
//        </td>
//        <td>
//            <a href="/book/delete/${book.id}">Delete</a>
//        </td>
//    </tr>
//`;
//        }
//    })
//        .catch ((error) => {
//        console.error(error.message);
//        document.getElementById("error_box").textContent = `Books fetch error: ${error.message}`
//        document.getElementById("error_box").hidden = false;
//    });
//
//}