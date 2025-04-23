import React from "react";
import WarehouseLocationSelector from "../../../components/WarehouseLocationSelector.jsx";

export default function ItemForm({ formData, onChange, categories = [], suppliers = [], readOnly = false, mode = "detail", onLocationSelect }) {
    return (
        <div className="grid grid-cols-2 gap-4 p-4 bg-white border rounded shadow">
            {/* 품목 기본 정보 */}
            <div>
                <label className="block font-semibold mb-1">품목명</label>
                <input
                    type="text"
                    name="name"
                    value={formData.name || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 고급 나사"
                    readOnly={readOnly}
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">분류 코드</label>
                <select
                    name="categoryId"
                    value={formData.categoryId || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    disabled={readOnly}
                >
                    <option value="">-- 분류 선택 --</option>
                    {categories.map((cat) => (
                        <option key={cat.id} value={cat.id} >
                            {cat.name}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <label className="block font-semibold mb-1">공급업체</label>
                <select
                    name="defaultSupplierId"
                    value={formData.defaultSupplierId || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    disabled={readOnly}
                >
                    <option value="">-- 공급업체 선택 --</option>
                    {suppliers.map((sup) => (
                        <option key={sup.id} value={sup.id} >
                            {sup.name}
                        </option>
                    ))}
                </select>
            </div>
            {mode !== "create" && (
                <div>
                    <label className="block font-semibold mb-1">품목 코드</label>
                    <input
                        type="text"
                        name="code"
                        value={formData.code || ""}
                        onChange={onChange}
                        className="w-full p-2 border rounded"
                        placeholder="예: A-1001"
                        readOnly={true}
                    />
                </div>
            )}
            <div>
                <label className="block font-semibold mb-1">초기 재고 수량</label>
                <input
                    type="number"
                    name="stock"
                    value={formData.stock || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    readOnly={readOnly}
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">단위</label>
                <input
                    type="text"
                    name="unit"
                    value={formData.unit || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: EA"
                    readOnly={readOnly}
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">단가</label>
                <input
                    type="number"
                    name="price"
                    value={formData.price || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 1000"
                    readOnly={readOnly}
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">원가</label>
                <input
                    type="number"
                    name="cost"
                    value={formData.cost || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 1000"
                    readOnly={readOnly}
                />
            </div>
            {/* 위치 정보: A-Frame 3D UI를 통한 창고 구역 선택 */}
            <div className="col-span-2">
                <label className="block font-semibold mb-1">위치 정보 (창고 구역 선택)</label>
                {/* create 모드인 경우, 사용자가 직접 선택 */}
                {!readOnly && (
                    <WarehouseLocationSelector
                        onSelect={(location) => {
                            onChange({target: {name: "location", value: JSON.stringify(location)}});
                        }}
                    />
                )}
                {readOnly && formData.location && (
                    <div className="p-2 border rounded bg-gray-50">
                        <strong>선택 위치:</strong> {formData.location}
                    </div>
                )}
            </div>
            {mode !== "create" && (
                <>
                    <div>
                        <label className="block font-semibold mb-1">LOT 번호</label>
                        <input
                            type="text"
                            name="lot"
                            value={formData.lot || ""}
                            onChange={onChange}
                            className="w-full p-2 border rounded"
                            placeholder="예: LOT-20240401"
                            readOnly={true}
                        />
                    </div>
                    <div>
                        <label className="block font-semibold mb-1">유통기한</label>
                        <input
                            type="date"
                            name="expirationDate"
                            value={formData.expirationDate || ""}
                            onChange={onChange}
                            className="w-full p-2 border rounded"
                            readOnly={true}
                        />
                    </div>
                </>
            )}
            <div className="col-span-2">
                <label className="block font-semibold mb-1">설명</label>
                <textarea
                    name="description"
                    value={formData.description || ""}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    rows="4"
                    placeholder="이 품목에 대한 설명을 입력하세요."
                    readOnly={readOnly}
                />
            </div>
        </div>
    );
}
