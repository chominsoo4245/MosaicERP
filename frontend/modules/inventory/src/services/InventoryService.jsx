import axios from 'axios';
import api from "./api.jsx";

export const getInventoryAPI = async (params) => {
  const response = await api.get(`/inventory-service`, {params});
  return response.data;
};

export const getInventoryDetailAPI = async (inventoryId, headers) => {
  const response = await api.get(`/inventory-service/detail/${inventoryId}`);
  return response.data;
}

export const searchInventoryAPI = async (params) => {
  const response = await api.get('inventory-service/search', {params});
  return response.data;
}

export const increaseInventoryAPI = async (data) => {
  const response = await api.post(`/inventory-service/increase`, data);
  return response.data;
};

export const decreaseInventoryAPI = async (data) => {
  const response = await api.post(`/inventory-service/decrease`, data);
  return response.data;
};

export const getInventoryHistoryAPI = async (params) => {
  const response = await api.get(`/inventory-service/history`, { params });
  return response.data;
};