import api from "./api.jsx";

export const getItemListAPI = async () => {
    const response = await api.get(`/item-bff/getItemList`);
    return response.data;
}

export const getItemAPI = async (id) => {
    const response = await api.get(`/item-bff/${id}`);
    return response.data;
}

export const getFormInitDataAPI = async () => {
    const response = await api.get("/item-bff/formDataInit");
    return response.data;
}

export const createItemAPI = async (formData) => {
    const response = await api.post("/item-bff/add", formData);
    return response.data;
}