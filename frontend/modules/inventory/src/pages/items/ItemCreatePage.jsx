import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ItemForm from "./form/ItemForm.jsx";
import { createItemAPI, getFormInitDataAPI } from "../../services/ItemService.jsx";

export default function ItemCreatePage() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: "",
        code: "", // 자동 생성됨; 생성 시에는 빈 값이나 "자동 생성됨"으로 처리
        stock: "",
        unit: "",
        price: "",
        cost: "",
        categoryId: "",
        defaultSupplierId: "",
        location: "",
        lot: "",         // 자동 생성됨
        expirationDate: "",  // 자동 생성됨
        description: ""
    });
    const [categories, setCategories] = useState([]);
    const [suppliers, setSuppliers] = useState([]);

    useEffect(() => {
        const fetchInitData = async () => {
            try {
                const response = await getFormInitDataAPI();
                setCategories(response.data.categories);
                setSuppliers(response.data.suppliers);
            } catch (e) {
                console.error("폼 초기 데이터 로딩 실패", e);
            }
        };
        fetchInitData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async () => {
        try {
            const response = await createItemAPI(formData);
            if (response.success) {
                navigate(`/items/detail/${response.data}`);
            } else {
                console.error("품목 등록 실패:", response.message);
            }
        } catch (error) {
            console.error("품목 등록 에러:", error);
        }
    };

    const handleCancel = () => {
        setFormData({
            name: "", code: "", stock: "", unit: "", price: "", cost: "",
            categoryId: "", defaultSupplierId: "", location: "", lot: "",
            expirationDate: "", description: ""
        });
    };

    const actions = [
        { label: "등록", type: "submit", onClick: handleSubmit },
        { label: "취소", type: "cancel", onClick: handleCancel },
    ];

    return (
        <PageLayout title="📦 품목 등록" breadcrumb={["품목 관리", "품목 등록"]} actions={<ButtonLayout actions={actions} />}>
            {/* 생성(Create) 모드에서는 mode="create"를 전달하여 자동 생성 필드는 숨김 처리 */}
            <ItemForm formData={formData} onChange={handleChange} categories={categories} suppliers={suppliers} readOnly={false} mode="create" />
        </PageLayout>
    );
}
