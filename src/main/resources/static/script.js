const API_URL = "http://localhost:8080/api/inspections";
let globalData = [];

// Fetch batch inspection details from Controller & DB
async function fetchInspections() {
    try {
        const res = await fetch(API_URL);
        globalData = await res.json();
        updateMetrics(globalData);
        renderTable(globalData);
    } catch (err) {
        console.error("Error fetching inspections:", err);
    }
}

// Update KPI Summary Cards with Split Quantities
function updateMetrics(data) {
    document.getElementById("statTotal").innerText = data.length;
    document.getElementById("statPass").innerText = data.filter(i => i.resultStatus === 'PASS').length;
    document.getElementById("statFail").innerText = data.filter(i => i.resultStatus === 'FAIL').length;
    document.getElementById("statRework").innerText = data.filter(i => i.resultStatus === 'REWORK').length;

    // Defect Qty: FAIL + REWORK
    const defectQty = data
        .filter(i => i.resultStatus === 'FAIL' || i.resultStatus === 'REWORK')
        .reduce((sum, item) => sum + (item.defectiveQuantity || 0), 0);

    // Total Batch Qty: PASS + FAIL + REWORK
    const totalQty = data
        .reduce((sum, item) => sum + (item.defectiveQuantity || 0), 0);

    document.getElementById("statDefectQty").innerText = defectQty;
    document.getElementById("statTotalQty").innerText = totalQty;
}

// Show/Hide Planning Manager Notice on Status Change
function checkReworkNotice(status) {
    const notice = document.getElementById("managerNotice");
    if (status === 'REWORK') {
        notice.style.display = "block";
    } else {
        notice.style.display = "none";
    }
}

// Render History Table dynamically
function renderTable(data) {
    const tableBody = document.getElementById("inspectionTableBody");
    tableBody.innerHTML = "";

    if (data.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="9" class="text-center py-3 text-muted">No records found.</td></tr>`;
        return;
    }

    data.forEach(item => {
        let badgeHtml = '';
        if (item.resultStatus === 'FAIL') {
            badgeHtml = `<span class="badge bg-danger">${item.resultStatus}</span>`;
        } else if (item.resultStatus === 'REWORK') {
            badgeHtml = `<span class="badge bg-warning text-dark" style="cursor: pointer;" onclick="approveBatch(${item.id})" title="Click to Approve Batch & Mark PASS">${item.resultStatus} 🔄</span>`;
        } else {
            badgeHtml = `<span class="badge bg-success">${item.resultStatus}</span>`;
        }

        let photoHtml = item.photoUrl
            ? `<img src="${item.photoUrl}" style="max-height: 40px; cursor: pointer;" class="rounded shadow-sm" onclick="showModalImage('${item.photoUrl}')" alt="Defect Photo">`
            : '-';

        tableBody.innerHTML += `
            <tr>
                <td class="fw-bold">${item.batchId}</td>
                <td>${item.inspectorName}</td>
                <td>${badgeHtml}</td>
                <td>${item.defectType || '-'}</td>
                <td>${photoHtml}</td>
                <td>${item.severity}</td>
                <td>${item.defectiveQuantity}</td>
                <td>${new Date(item.inspectionDate).toLocaleString()}</td>
                <td>
                    <button class="btn btn-sm btn-outline-warning text-dark me-1" onclick="flagRework(${item.id})">Flag Rework</button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteInspection(${item.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

// Real-time Search & Filter
function filterInspections() {
    const searchText = document.getElementById("searchInput").value.toLowerCase();
    const selectedStatus = document.getElementById("filterStatus").value;

    const filtered = globalData.filter(item => {
        const matchesBatch = item.batchId.toLowerCase().includes(searchText);
        const matchesStatus = (selectedStatus === 'ALL') || (item.resultStatus === selectedStatus);
        return matchesBatch && matchesStatus;
    });

    renderTable(filtered);
}

// Photo Modal Preview
function showModalImage(url) {
    document.getElementById("modalImage").src = url;
    const myModal = new bootstrap.Modal(document.getElementById('imageModal'));
    myModal.show();
}

// Record Inspection Form Submit
document.getElementById("inspectionForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const batchId = document.getElementById("batchId").value.trim();
    const inspectorName = document.getElementById("inspectorName").value.trim();
    const resultStatus = document.getElementById("resultStatus").value.trim();
    const defectType = document.getElementById("defectType").value;
    const severity = document.getElementById("severity").value.trim();
    const defectiveQuantity = document.getElementById("defectiveQuantity").value.trim();
    const remarks = document.getElementById("remarks").value.trim();

    if (!batchId || !inspectorName || !resultStatus || !defectType || !severity || !defectiveQuantity || !remarks) {
        alert("Please fill all required fields!");
        return;
    }

    const fileInput = document.getElementById("photoFile");
    let photoUrl = "";

    if (fileInput.files.length > 0) {
        photoUrl = await compressImage(fileInput.files[0]);
    }

    const payload = {
        batchId: batchId,
        inspectorName: inspectorName,
        resultStatus: resultStatus,
        defectType: defectType,
        severity: severity,
        defectiveQuantity: parseInt(defectiveQuantity),
        remarks: remarks,
        photoUrl: photoUrl
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            document.getElementById("inspectionForm").reset();
            document.getElementById("managerNotice").style.display = "none";
            fetchInspections();
        } else {
            alert("Error saving inspection record.");
        }
    } catch (error) {
        console.error("Submission failed:", error);
    }
});

// Canvas Image Compression
function compressImage(file) {
    return new Promise((resolve) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (event) => {
            const img = new Image();
            img.src = event.target.result;
            img.onload = () => {
                const canvas = document.createElement('canvas');
                const maxWidth = 300;
                const scaleSize = maxWidth / img.width;
                canvas.width = maxWidth;
                canvas.height = img.height * scaleSize;

                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

                resolve(canvas.toDataURL('image/jpeg', 0.7));
            };
        };
    });
}

// Flag Rework
async function flagRework(id) {
    const confirmRework = confirm("Are you sure you want to flag this batch for REWORK? Production Planning Manager will be notified.");
    if (!confirmRework) return;

    const res = await fetch(`${API_URL}/${id}`);
    if (res.ok) {
        const item = await res.json();
        item.resultStatus = "REWORK";
        await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(item)
        });
        alert("Batch flagged for REWORK! Production Planning Manager notified successfully.");
        fetchInspections();
    }
}

// Approve Batch
async function approveBatch(id) {
    const confirmPass = confirm("Verify quality standards met? Click OK to approve batch and mark as PASS.");
    if (confirmPass) {
        const res = await fetch(`${API_URL}/${id}`);
        if (res.ok) {
            const item = await res.json();
            item.resultStatus = "PASS";
            await fetch(API_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(item)
            });
            alert("Quality standards met! Batch approved as PASS.");
            fetchInspections();
        }
    }
}

// Delete Record
async function deleteInspection(id) {
    if (confirm("Are you sure you want to delete this inspection record?")) {
        await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        fetchInspections();
    }
}

// Reports Exporting Functions
function exportToCSV() {
    if (globalData.length === 0) return alert("No data to export!");
    let csvContent = "data:text/csv;charset=utf-8,Batch ID,Inspector Name,Status,Defect Type,Severity,Defective Qty,Inspection Date,Remarks\n";
    globalData.forEach(item => {
        let date = new Date(item.inspectionDate).toLocaleString().replace(/,/g, '');
        let remarks = (item.remarks || "").replace(/,/g, ' ');
        csvContent += `${item.batchId},${item.inspectorName},${item.resultStatus},${item.defectType || 'None'},${item.severity},${item.defectiveQuantity},${date},${remarks}\n`;
    });
    const link = document.createElement("a");
    link.setAttribute("href", encodeURI(csvContent));
    link.setAttribute("download", `Inspection_Report_${new Date().toISOString().slice(0,10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function exportToPDF() {
    if (globalData.length === 0) return alert("No data to export!");
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    doc.setFontSize(16);
    doc.text("Garment Quality Assurance - Inspection Log Report", 14, 15);
    const headers = [["Batch ID", "Inspector", "Status", "Defect Type", "Severity", "Qty", "Date"]];
    const rows = globalData.map(item => [
        item.batchId, item.inspectorName, item.resultStatus, item.defectType || 'None', item.severity, item.defectiveQuantity, new Date(item.inspectionDate).toLocaleDateString()
    ]);
    doc.autoTable({ head: headers, body: rows, startY: 25, theme: 'grid', headStyles: { fillColor: [15, 23, 42] } });
    doc.save(`QA_Report_${new Date().toISOString().slice(0,10)}.pdf`);
}

fetchInspections();