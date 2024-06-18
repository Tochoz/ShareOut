@echo off
setlocal enableextensions enabledelayedexpansion

set /p filename="Enter the component name: "

(
echo section.%filename%
echo.
echo @media ^(max-width: 1100px^)
echo.
echo @media ^(max-width: 500px^)
) > "files\css\%filename%.css"
(
echo ^<link rel="stylesheet" href="/static/css/%filename%.css"^>
echo ^{%% block custom_styles %%^}^{%% endblock %%^}
)> "src\main\resources\templates\components\%filename%.peb"

echo Gooooooood!