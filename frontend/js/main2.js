$(document).ready(function() {
    let currentPage = 1;
    let currentType = 'ban';

    function fetchPunishments(type, page) {
        const spinner = $("#spinner");
        const punishments = $("#punishments");

        $.ajax({
            url: "/history/" + type + "/" + page,
            method: "GET",
            beforeSend: function() {
                spinner.show();
                punishments.hide();
            },
            success: function(response) {
                punishments.empty();

                // let bright = false;
                // if (response.punishments.size() % 2 == 0) {

                // }

                if (response.punishments != null) {
                    let light = true;
                    if (response.punishments.length % 2 == 0) {
                        light = false;
                    }

                    response.punishments.forEach(function(punishment) {
                        let statusBadge;
                        let line;
                        if (punishment.label == "Permanent") {
                            line = '<div class="col-auto permanent-line"></div>';
                            statusBadge = '<span class="permanent fw-medium">Permanent</span>';
                        } else if (punishment.label == "Active") {
                            line = '<div class="col-auto active-line"></div>';
                            statusBadge = '<span class="active fw-medium">Active</span>';
                        } else {
                            line = '<div class="col-auto expired-line"></div>';
                            statusBadge = '<span class="expired fw-medium">Expired</span>';
                        }

                        let row;
                        if (light) {
                            row = '<div class="row align-items-center p-3 flex-nowrap light-row">';
                        } else {
                            row = '<div class="row align-items-center p-3 flex-nowrap">';
                        }

                        const html = `
                        ${row}
                        ${line}
                        <div class="col-auto">
                           <img src="https://visage.surgeplay.com/face/55/${punishment.victimUuid}">
                        </div>
                        <div class="col">
                           <p class="fw-medium fs-5 mb-0">Offender</p>
                           <p class="fw-light">${punishment.victimUsername}</p>
                        </div>
                        <div class="col-auto">
                           <img src="https://visage.surgeplay.com/face/55/${punishment.operatorUuid}">
                        </div>
                        <div class="col">
                           <p class="fw-medium fs-5 mb-0">Staff</p>
                           <p class="fw-light">${punishment.operatorUsername}</p>
                        </div>
                        <div class="col">
                           <p class="fw-medium fs-5 mb-0">Reason</p>
                           <p class="fw-light">${punishment.reason}</p>
                        </div>
                        <div class="col">
                            ${statusBadge}
                        </div>
                        </div>
                        `;
                        punishments.append(html);

                        if (light == true) {
                            light = false;
                        } else {
                            light = true;
                        }
                    });
                } else {
                    const html = `<div class="text-center p-5"> <p class="fw-medium fs-5 mb-0">Nothing to show for now</p></div>`;
                    punishments.append(html);
                }
                if (currentPage >= response.pageCount) {
                    $('#nextBtn').prop('disabled', true);
                    $('#nextBtn').addClass('disabled-btn');
                } else {
                    $('#nextBtn').prop('disabled', false);
                    $('#nextBtn').removeClass('disabled-btn');
                }

                if (currentPage <= 1) {
                    $('#prevBtn').prop('disabled', true);
                    $('#prevBtn').addClass('disabled-btn');
                } else {
                    $('#prevBtn').prop('disabled', false);
                    $('#prevBtn').removeClass('disabled-btn');
                }
            },
            error: function() {
                console.log("Error retreiving punishment history");
            },
            complete: function() {
                spinner.hide();
                punishments.show();
            }
        });
    }

    function removeActiveClass() {
        $('#banType').parent().removeClass('navbar-active');
        $('#kickType').parent().removeClass('navbar-active');
        $('#muteType').parent().removeClass('navbar-active');
        $('#warnType').parent().removeClass('navbar-active');
    }

    fetchPunishments(currentType, currentPage);

    $('#nextBtn').click(function() {
        currentPage++;
        fetchPunishments(currentType, currentPage);
    });

    $('#prevBtn').click(function() {
        currentPage--;
        fetchPunishments(currentType, currentPage);
    });

    $('#banType').click(function() {
        currentPage = 1;
        currentType = 'ban';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        $('#banType').parent().addClass('navbar-active');
    });

    $('#kickType').click(function() {
        currentPage = 1;
        currentType = 'kick';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        $('#kickType').parent().addClass('navbar-active');
    });

    $('#muteType').click(function() {
        currentPage = 1;
        currentType = 'mute';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        $('#muteType').parent().addClass('navbar-active');
    });

    $('#warnType').click(function() {
        currentPage = 1;
        currentType = 'warn';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        $('#warnType').parent().addClass('navbar-active');
    });
});