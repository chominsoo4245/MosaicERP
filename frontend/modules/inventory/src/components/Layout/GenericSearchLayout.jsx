import {useState} from "react";

const GenericSearchLayout = ({ fields, initialValues = {}, onSearch }) => {
    const [formData, setFormData] = useState(initialValues);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData({...formData, [name]: value});
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        onSearch(formData)
    }


    return (
        <form onSubmit={handleSubmit} className="bg-white shadow rounded p-4 mb-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {fields.map((field) => (
                    <div key={field.name}>
                        <label htmlFor={field.name} className="block text-sm font-medium text-gray-700">
                            {field.label}
                        </label>
                        <input
                            id={field.name}
                            type={field.type}
                            name={field.name}
                            placeholder={field.placeholder}
                            value={formData[field.name] || ""}
                            onChange={handleChange}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                        />
                    </div>
                ))}
            </div>
            <div className="mt-4 text-right">
                <button
                    type="submit"
                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                    검색
                </button>
            </div>
        </form>
    );
};

export default GenericSearchLayout;