$(document).ready(function () {
    let currentPage = 1;
    let currentType = 'ban';
    let morePages = true;

    async function getWithAsyncFetch(url) {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    }

    function fetchTypeStats(type) {
        let typeText = "Punishment type";
        let dataStats = "0";

        const data = getWithAsyncFetch(`/stats/${type}`);

        if (data != null) {
            switch (data.type) {
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
            dataStats = data.stats;
        }

        $('#type-stats-text').text(`${typeText} (${dataStats})`);
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
        if (currentPage > 1) {
            currentPage--;
        }
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
            let pageCountValue = Number($("#pageCount"));

            if (isNaN(pageCountValue)) {
                return false;
            }

            // The previous call should ensure that the pageCountValue variable is a number
            // but leaving this here for safety.
            if (typeof pageCountValue != 'number') {
                return false;
            }

            if (pageCountValue < 1) {
                return false;
            }

            if (pageCountValue > 999) {
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

    function setPunishmentStyle(length, row) {
        if (length === "Permanent") {
            $(`#line-upper-${rowCount}`).addClass('permanent-line');
            $(`#status-badge-${rowCount}`).addClass('permanent');
            $(`#line-below-${rowCount}`).addClass('permanent-line');
        } else if (length === "Active") {
            $(`#line-upper-${rowCount}`).addClass('active-line');
            $(`#status-badge-${rowCount}`).addClass('active');
            $(`#line-below-${rowCount}`).addClass('active-line');
        } else {
            $(`#line-upper-${rowCount}`).addClass('expired-line');
            $(`#status-badge-${rowCount}`).addClass('expired');
            $(`#line-below-${rowCount}`).addClass('expired-line');
        }
    }

    function setRowData(victimUUID, victimUsername, operatorUUID, operatorUsername, reason, row) {
        $(`#first-uuid-${row}`).attr('src', `https://visage.surgeplay.com/face/55/${victimUUID}`);
        $(`#offender-${row}`).text(`${victimUsername}`);
        $(`#second-uuid-${row}`).attr('src', `https://visage.surgeplay.com/face/55/${operatorUUID}`);
        $(`#operator-${row}`).text(`${operatorUsername}`);
        $(`#reason-${row}`).text(`${reason}`);
    }

    function setMorePages(morePages) {
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
    }

    function fetchPunishments(type, page) {
        const spinner = $("#punishments-spinner");
        const punishments = $("#punishments");

        // First we hide everything.
        spinner.show();
        $(`#nothing-to-show`).hide()

        // Since we have 6 pre-defined html well objects, we need to hide them all.
        for (let i = 0; i < 6; i++) {
            $(`#punishments-${i}`).hide();
        }

        updatePageCount();

        // Update the punishment type and stats variables
        fetchTypeStats(type);

        // We get the data.
        const data = getWithAsyncFetch(`/punishments/" + ${type} + "/" + ${page}`);

        // We actually got data!
        if (data.punishments != null) {

            let rowCount = 0;
            for (const key in data.punishments) {
                setPunishmentStyle(data.punishments[key].label, rowCount);

                let operatorUUID = data.punishments[key].operatorUuid;
                if (operatorUUID === '00000000-0000-0000-0000-000000000000') {
                    operatorUUID = "console";
                }

                setRowData(data.punishments[key].victimUuid, data.punishments[key].victimUsername, operatorUUID, data.punishments[key].operatorUsername, data.punishments[key].reason, rowCount);
                rowCount++;
            }

            setMorePages(data.punishments.morePages);

            // Finished populating values, let's show the data.
            spinner.hide();
            for (let i = 0; i < rowCount; i++) {
                $(`#punishments-${i}`).show();
            }

        } else {
            // We didn't get any data.
            $(`#nothing-to-show`).show();
        }
    }
});