$(document).ready(function () {
    let currentPage = 1;
    let currentType = 'ban';
    let morePages = true;

    function fetchTypeStats(type) {
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


                $('#type-stats-text').text(`${typeText} (${response.stats})`);

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

    function fetchPunishments(type, page) {
        const spinner = $("#punishments-spinner");
        const punishments = $("#punishments");

        $.ajax({
            url: "/punishments/" + type + "/" + page,
            method: "GET",
            beforeSend: function() {
                spinner.show();
                for (let i = 0; i < 6; i++) {
                    $(`#punishments-${i}`).hide();
                }
                $(`#nothing-to-show`).hide();
            },
            success: function(response) {
                fetchTypeStats(type);
                updatePageCount();

                if (response.punishments != null) {
                    morePages = response.morePages;

                    let rowCount = 0;
                    response.punishments.forEach(function(punishment) {

                        let operatorUuid = punishment.operatorUuid;
                        if (punishment.label == "Permanent") {
                            $(`#line-upper-${rowCount}`).addClass('permanent-line');
                            $(`#status-badge-${rowCount}`).addClass('permanent');
                            $(`#line-below-${rowCount}`).addClass('permanent-line');
                        } else if (punishment.label == "Active") {
                            $(`#line-upper-${rowCount}`).addClass('active-line');
                            $(`#status-badge-${rowCount}`).addClass('active');
                            $(`#line-below-${rowCount}`).addClass('active-line');
                        } else {
                            $(`#line-upper-${rowCount}`).addClass('expired-line');
                            $(`#status-badge-${rowCount}`).addClass('expired');
                            $(`#line-below-${rowCount}`).addClass('expired-line');
                        }

                        if (operatorUuid == '00000000-0000-0000-0000-000000000000') {
                            operatorUuid = "console";
                        }

                        $(`#first-uuid-${rowCount}`).attr('src', `https://visage.surgeplay.com/face/55/${punishment.victimUuid}`);
                        $(`#offender-${rowCount}`).text(`${punishment.victimUsername}`);
                        $(`#second-uuid-${rowCount}`).attr('src', `https://visage.surgeplay.com/face/55/${operatorUuid}`);
                        $(`#operator-${rowCount}`).text(`${punishment.operatorUsername}`);
                        $(`#reason-${rowCount}`).text(`${punishment.reason}`);

                        rowCount++;
                    });
                } else {
                    for (let i = 0; i < 6; i++) {
                        $(`#punishments-${i}`).hide();
                    }
                    $(`#nothing-to-show`).show();
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
                $(`#nothing-to-show`).show();

            },
            complete: function() {
                spinner.hide();
                for (let i = 0; i < 6; i++) {
                    $(`#punishments-${i}`).show();
                }
            }
        });
    }

});