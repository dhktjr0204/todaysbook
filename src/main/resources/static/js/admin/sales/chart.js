document.addEventListener('DOMContentLoaded', function () {

    getSalesData();
});

const monthNames = {
    "JANUARY": "1월",
    "FEBRUARY": "2월",
    "MARCH": "3월",
    "APRIL": "4월",
    "MAY": "5월",
    "JUNE": "6월",
    "JULY": "7월",
    "AUGUST": "8월",
    "SEPTEMBER": "9월",
    "OCTOBER": "10월",
    "NOVEMBER": "11월",
    "DECEMBER": "12월"
};

const yearSelect = document.getElementById("yearSelect");
let salesData = [];

yearSelect.addEventListener("change", function() {

    const selectedYear = yearSelect.value;

    getSalesData(selectedYear);
});

function getSalesData() {

    let url = '/sales';
    let type = 'GET';

    let year = yearSelect.value;

    $.ajax({

        url: url,
        type: type,
        data: {
            year: year
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

        labels.push(monthNames[item.month]);
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
    let myChart = new Chart(ctx, {
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
        }
    });
}