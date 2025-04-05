import React from "react";

export default function PageLayout({ title, breadcrumb, actions, children }) {
    return (
        <div className="p-6 flex flex-col gap-4">
            <div className="flex justify-between items-center">
                <div>
                    <h2 className="text-2xl font-semibold">{title}</h2>
                    {breadcrumb && (
                        <div className="text-sm text-gray-500 mt-1">
                            {breadcrumb.join(" > ")}
                        </div>
                    )}
                </div>
                {actions && <div>{actions}</div>}
            </div>

            <div className="bg-white rounded-lg shadow p-4">{children}</div>
        </div>
    );
}