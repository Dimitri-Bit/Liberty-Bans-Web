$(document).ready(function() {
    let currentPage = 1;
    let currentType = 'ban';

    function fetchPunishments(type, page) {
        const spinner = $("#spinner");
        const punishments = $("#punishments");

        $.ajax({
            url: '/history/' + type + "/" + page,
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
                        if (punishment.label == "Permanent") {
                            statusBadge = '<span class="badge bg-danger">Permanent</span>';
                        } else if (punishment.label == "Expired") {
                            statusBadge = '<span class="badge bg-success">Expired</span>';
                        } else {
                            statusBadge = '<span class="badge bg-primary">Active</span>';
                        }

                        const html = `
                          <div class="row align-items-center p-4 flex-nowrap">
                            <div class="col-auto">
                              <img src="https://visage.surgeplay.com/face/50/${punishment.victimUuid}" alt="">
                            </div>
                            <div class="col">
                              <h5 class="mb-0">${punishment.victimUsername}</h5>
                              <p class="mb-0">(Offender)</p>
                            </div>
                            <div class="col-auto">
                              <img src="https://visage.surgeplay.com/face/50/${punishment.operatorUuid}" alt="">
                            </div>
                            <div class="col">
                              <h5 class="mb-0">${punishment.operatorUsername}</h5>
                              <p class="mb-0">(Staff)</p>
                            </div>
                            <div class="col-auto"></div>
                            <div class="col">
                              <h5 class="mb-0">Reason</h5>
                              <p class="mb-0">${punishment.reason}</p>
                            </div>
                            <div class="col-auto"></div>
                            <div class="col">
                              <h5 class="mb-0">${statusBadge}</h5>
                              <p class="mb-0"></p>
                            </div>
                          </div>`;
                        punishments.append(html); // Populate punishment items
                    });
                } else {
                    const html = `<div class="text-center p-5"> <h2>Nothing to show for now</h2></div>`;
                    punishments.append(html);
                }

                if (currentPage >= response.pageCount) {
                    $('#nextBtn').prop('disabled', true);
                } else {
                    $('#nextBtn').prop('disabled', false);
                }

                if (currentPage <= 1) {
                    $('#prevBtn').prop('disabled', true);
                } else {
                    $('#prevBtn').prop('disabled', false);
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
    });

    $('#kickType').click(function() {
        currentPage = 1;
        currentType = 'kick';
        fetchPunishments(currentType, currentPage);
    });

    $('#muteType').click(function() {
        currentPage = 1;
        currentType = 'mute';
        fetchPunishments(currentType, currentPage);
    });

    $('#warnType').click(function() {
        currentPage = 1;
        currentType = 'warn';
        fetchPunishments(currentType, currentPage);
    });
});