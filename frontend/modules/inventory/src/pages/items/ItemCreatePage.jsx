import { useState, useEffect } from "react";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ItemForm from "./form/ItemForm.jsx";
import {createItemAPI, getFormInitDataAPI} from "../../services/ItemService.jsx";

export default function ItemCreatePage() {
    const [formData, setFormData] = useState({
        name: "", code: "", stock: "", unit: "", price: "", cost: "",
        categoryId: "", defaultSupplierId: "", location: "", lot: "",
        expirationDate: "", description: ""
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
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        const response = createItemAPI(formData);
        console.log(response);
        // TODO: API 호출
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
        <PageLayout
            title="📦 품목 등록"
            breadcrumb={["품목 관리", "품목 등록"]}
            actions={<ButtonLayout actions={actions} />}
        >
            <ItemForm
                formData={formData}
                onChange={handleChange}
                categories={categories}
                suppliers={suppliers}
            />
        </PageLayout>
    );
}
