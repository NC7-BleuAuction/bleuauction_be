
function sendAjaxRequest(url, method, data, successCallback, errorCallback) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        success: successCallback,
        error: errorCallback
    });
}

function sendAxiosRequest(url, method, params, successCallback, errorCallback) {
    axios({
        url: url,
        method: method,
        params: params,
    }).then(successCallback).catch(errorCallback);
}