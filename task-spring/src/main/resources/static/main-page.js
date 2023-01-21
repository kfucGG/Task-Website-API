

function openNewTaskModal() {
    let modal = new bootstrap.Modal(document.querySelector("#modalWindow"))
    modal.show();
}

function closeNewTaskModal() {
    $('#modalWindow').modal('toggle')
}

function sendTaskDataOnServer () {

    let form = document.getElementById("modal-content");
    form.addEventListener("button",a => {
        a.preventDefault();

        let formData = new FormData(form);
        let name = formData.get("task-name");
        let time = formData.get("task-time");
        console.log(name);
        console.log(time);
    });
}