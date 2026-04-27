(function () {
    function addSidebarToggle() {
        const sideNav = document.getElementById("side-nav");
        const docContent = document.getElementById("doc-content");
        if (!sideNav || !docContent || document.getElementById("sidebar-toggle")) {
            return;
        }
        const button = document.createElement("button");
        button.id = "sidebar-toggle";
        button.type = "button";
        const defaultButtonWidth = 140;
        const buttonGap = 14;
        const minimumLeftPosition = 18;
        const updatePosition = () => {
            if (document.body.classList.contains("sidebar-collapsed")) {
                button.style.left = `${minimumLeftPosition}px`;
                return;
            }
            const sideNavWidth = sideNav.getBoundingClientRect().width;
            const buttonWidth = button.getBoundingClientRect().width || defaultButtonWidth;
            if (sideNavWidth < buttonWidth + (buttonGap * 2)) {
                button.style.left = `${minimumLeftPosition}px`;
                return;
            }
            button.style.left = `${Math.max(minimumLeftPosition, sideNavWidth - buttonWidth - buttonGap)}px`;
        };
        const setState = (collapsed) => {
            document.body.classList.toggle("sidebar-collapsed", collapsed);
            button.textContent = collapsed ? "Show navigation" : "Hide navigation";
            button.setAttribute("aria-expanded", String(!collapsed));
            window.requestAnimationFrame(updatePosition);
            try {
                window.localStorage.setItem("doxygen-sidebar-collapsed", collapsed ? "true" : "false");
            } catch (e) {
                // Ignore storage restrictions in local file previews.
            }
        };
        button.addEventListener("click", () => setState(!document.body.classList.contains("sidebar-collapsed")));
        document.body.appendChild(button);
        let collapsed = false;
        try {
            collapsed = window.localStorage.getItem("doxygen-sidebar-collapsed") === "true";
        } catch (e) {
            collapsed = false;
        }
        setState(collapsed);
        window.addEventListener("resize", updatePosition);
        if (window.javafxToolSidebarToggleObserver) {
            window.javafxToolSidebarToggleObserver.disconnect();
        }
        if (typeof ResizeObserver !== "undefined") {
            window.javafxToolSidebarToggleObserver = new ResizeObserver(updatePosition);
            window.javafxToolSidebarToggleObserver.observe(sideNav);
        }
    }

    document.addEventListener("DOMContentLoaded", () => {
        addSidebarToggle();
    });
}());
