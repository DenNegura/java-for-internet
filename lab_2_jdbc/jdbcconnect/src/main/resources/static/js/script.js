function clearForm() {
    document.getElementById('sort1').checked = false;
    document.getElementById('sort2').checked = false;
    document.getElementById('titleInput').value = "";
    document.getElementById('categoryInput').value = "";
    document.getElementById('minInput').value = "";
    document.getElementById('maxInput').value = "";
}
var lastList;
function inputIdDish(idDish) {
    var currentList = document.getElementById("dishInfo" + idDish);
    if(lastList != null) {
        lastList.classList.add('ingredients');
    }
    currentList.classList.remove('ingredients');
    lastList = currentList;
}