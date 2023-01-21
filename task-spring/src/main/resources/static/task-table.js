let url = "http://localhost:7070/tasks/1";
fillTaskTable();

function fillTaskTable() {
    fetch(url).then(response => response.json()).then(data => data.forEach(task => {
        let content = `$(
                <tr>
                    <td>${task.id}</td>
                    <td>${task.taskName}</td>
                    <td>Delete</td>
                </tr>
            )`;
        $("#taskTableContent").append(content);
    }));
}

