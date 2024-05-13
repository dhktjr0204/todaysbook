document.addEventListener('DOMContentLoaded', function () {

    getSalesData();
});

const yearSelect = document.getElementById("yearSelect");
const monthSelect = document.getElementById("monthSelect");

let salesData = [];

yearSelect.addEventListener("change", handleChange);
monthSelect.addEventListener("change", handleChange);

function handleChange() {
    getSalesData();
}

function getSalesData() {

    let url = '/sales/category';
    let type = 'GET';

    let year = yearSelect.value;
    let month = monthSelect.value;

    $.ajax({

        url: url,
        type: type,
        data: {
            year: year,
            month: month
        },
        success: function (response) {

            salesData = response;

            setChartData();
        }
    });
}

function setChartData() {

    let labels = [];
    let sales = [];

    salesData.forEach(item => {

        labels.push(item.categoryName);
        sales.push(item.sales);
    });

    removePreviousChart();
    drawNewChart(labels, sales);
}

function removePreviousChart() {

    const salesChartCanvas = document.getElementById("salesChart");

    const salesChartParent = salesChartCanvas.parentNode;

    salesChartParent.removeChild(salesChartCanvas);

    const newSalesChartCanvas = document.createElement("canvas");
    newSalesChartCanvas.id = "salesChart";
    salesChartParent.appendChild(newSalesChartCanvas);
}

function drawNewChart(label, data) {

    let ctx = document.getElementById('salesChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: label,
            datasets: [{
                label: '매출액',
                data: data,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options:{
            responsive: false
        }
    });
}