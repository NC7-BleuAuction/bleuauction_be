function sendAxiosRequest(url, method, params, successCallback, errorCallback) {
    const axiosConfig = {
        url: url,
        method: method,
    };
    if (params != null)
        axiosConfig.params = params;
    axios(axiosConfig).then(successCallback).catch(errorCallback);
}

function formToJson(form) {
    const formData = new FormData(form);
    const jsonObj = {};
    for (let [key, value] of formData.entries()) {
        jsonObj[key] = value;
    }
    return jsonObj;
}

function dateFormatParse(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
}