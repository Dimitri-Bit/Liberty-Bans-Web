$(document).ready(function() {
    let currentPage = 1;
    let currentType = 'ban';
    let morePages = true;

    function fetchTotalStats() {
        const allStats = $("#all-stats");

        $.ajax({
            url: "/stats/all",
            method: "GET",
            success: function(response) {
                allStats.html(`${response.stats} <i class="far fa-chart-bar"></i>`);
                console.log("test");
            },
            error: function() {
                console.log("Error retrieving total stats");
            }
        })
    }

    function fetchTypeStats(type) {
        const typeStats = $("#type-stats");
        let typeText = "Bans";

        $.ajax({
            url: `/stats/${type}`,
            method: "GET",
            success: function(response) {

                switch (type) {
                    case "ban":
                        typeText = "Bans";
                        break;

                    case "mute":
                        typeText = "Mutes";
                        break;

                    case "kick":
                        typeText = "Kicks";
                        break;

                    case "warn":
                        typeText = "Warns";
                        break;
                }

                typeStats.html(`${typeText} <span class="fs-2">(${response.stats})</span>`);

            },
            error: function() {
                console.error("Error retrieving type stats");
            }
        })
    }

    function updatePageCount() {
        const pageCount = $("#pageCount");
        pageCount.val(currentPage);
    }

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
                fetchTypeStats(type);
                updatePageCount();

                punishments.empty();

                if (response.punishments != null) {
                    morePages = response.morePages;

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
                if (!morePages) {
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
                console.log("Error retrieving punishment history");
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

    fetchTotalStats();
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

    $('#pageCount').keypress(function(e) {
        if (e.which == 13) {
            let pageCountValue = $("#pageCount").val() * 1; // Weird way of turning a string into a number, don't ask me why. I'm tired.

            if (typeof pageCountValue != 'number') {
                return false;
            }

            if (!morePages && pageCountValue > currentPage) {
                return false;
            }

            if (pageCountValue == currentPage) {
                return false;
            }

            currentPage = pageCountValue;
            fetchPunishments(currentType, currentPage);
            return false;
        }
    });
});