$(document).ready(function() {
    var currentPage = 1;
    var currentType = 'ban';

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

                if (punishments.length > 0) {
                    response.punishments.forEach(function(punishment) {
                        const statusBadge = punishment.active ?
                            '<span class="badge bg-primary">Active</span>' :
                            '<span class="badge bg-success">Expired</span>';
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
});