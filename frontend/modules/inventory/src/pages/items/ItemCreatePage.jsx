import { useState } from "react";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ItemForm from "./form/ItemForm.jsx";

export default function ItemCreatePage() {
    const [formData, setFormData] = useState({
        name: "",
        code: "",
        stock: "",
        unit: "",
        price: "",
        category: "",
        supplier: "",
        location: "",
        lot: "",
        expirationDate: "",
        description: "",
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        console.log("등록 요청:", formData);
        // TODO: API 호출 추가
    };

    const handleCancel = () => {
        setFormData({
            name: "",
            code: "",
            stock: "",
            unit: "",
            price: "",
            category: "",
            supplier: "",
            location: "",
            lot: "",
            expirationDate: "",
            description: "",
        });
    };

    const actions = [
        { label: "등록", type: "submit", onClick: handleSubmit },
        { label: "취소", type: "cancel", onClick: handleCancel },
    ];

    return (
        <PageLayout
            title="📦 품목 등록"
            breadcrumb={["품목 관리", "품목 등록"]}
            actions={<ButtonLayout actions={actions} />}
        >
            <ItemForm formData={formData} onChange={handleChange} />
        </PageLayout>
    );
}