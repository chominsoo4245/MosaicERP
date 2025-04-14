import api from "./api.jsx";

export const getItemListAPI = async () => {
    const response = await api.get(`/item-bff/aggregated`);
    return response.data;
}

export const getFormInitDataAPI = async () => {
    const response = await api.get("/item-bff/form-init");
    return response.data;
}

export const createItemAPI = async (formData) => {
    const response = await api.post("/item-bff/add", formData);
    return response.data;
}