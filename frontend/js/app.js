$(document).ready(function() {
    let currentPage = 1;
    let currentType = 'ban';

    function fetchPunishments(type, page) {
        const spinner = $("#spinner");
        const punishments = $("#punishments");

        $.ajax({
            url: "/punishments/" + type + "/" + page,
            method: "GET",
            beforeSend: function() {
                spinner.show();
                punishments.hide();
            },
            success: function(response) {
                punishments.empty();

                if (response.punishments != null) {
                    response.punishments.forEach(function(punishment) {
                        let statusBadge;
                        let line;
                        let operatorUuid = punishment.operatorUuid;
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

                        if (operatorUuid == '00000000-0000-0000-0000-000000000000') {
                            operatorUuid = "console";
                        }

                        const html = `
                        <div class="row align-items-center p-3 flex-nowrap">
                        ${line}
                        <div class="col-auto">
                        <img src="https://visage.surgeplay.com/face/55/${punishment.victimUuid}">
                        </div>
                        <div class="col">
                        <p class="fs-5 mb-0">Offender</p>
                        <p>${punishment.victimUsername}</p>
                        </div>
                        <div class="col-auto">
                        <img src="https://visage.surgeplay.com/face/55/${operatorUuid}">
                        </div>
                        <div class="col">
                        <p class="fs-5 mb-0">Staff</p>
                        <p>${punishment.operatorUsername}</p>
                        </div>
                        <div class="col">
                        <div class="fs-5 mb-0">Reason</div>
                        <p>${punishment.reason}</p>
                        </div>
                        <div class="col">
                        ${statusBadge}
                        </div>
                        ${line}
                        </div>
                        `;
                        punishments.append(html);
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