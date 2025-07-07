import sys
import subprocess
import os
from pathlib import Path


def check_python_version():
    """Check if Python version is compatible."""
    print("Checking Python version...")

    version = sys.version_info
    print(f"Python version: {version.major}.{version.minor}.{version.micro}")

    if version.major < 3 or (version.major == 3 and version.minor < 7):
        print("❌ Python 3.7+ is required")
        print("Please upgrade Python and try again")
        return False

    print("✓ Python version is compatible")
    return True


def install_package():
    """Install the gbl-ninja-python package."""
    print("\nInstalling gbl-ninja-python package...")

    try:
        # Try to install the package
        result = subprocess.run(
            [sys.executable, "-m", "pip", "install", "gbl-ninja-python"],
            capture_output=True,
            text=True,
            check=True
        )

        print("✓ Package installed successfully")
        if result.stdout:
            print("Installation output:")
            print(result.stdout)

        return True

    except subprocess.CalledProcessError as e:
        print(f"❌ Installation failed: {e}")
        if e.stderr:
            print("Error details:")
            print(e.stderr)
        return False
    except Exception as e:
        print(f"❌ Unexpected error during installation: {e}")
        return False


def verify_installation():
    """Verify that the package is installed and working."""
    print("\nVerifying installation...")

    try:
        # Try to import the package
        import gbl
        print("✓ Package imported successfully")

        # Try to create a basic builder
        builder = gbl.Gbl().GblBuilder.create()
        print("✓ GBL builder created successfully")

        # Try to add a simple tag
        builder.application(type_val=32, version=0x10000)
        print("✓ Basic operations working")

        # Check available tag types
        tag_types = builder.get_tag_types()
        print(f"✓ Found {len(tag_types)} tag types")

        return True

    except ImportError as e:
        print(f"❌ Import failed: {e}")
        return False
    except Exception as e:
        print(f"❌ Verification failed: {e}")
        return False


def check_demo_files():
    """Check if demo files are present."""
    print("\nChecking demo files...")

    required_files = [
        "create_gbl_examples.py",
        "parse_gbl_examples.py",
        "run_all_examples.py",
        "requirements.txt",
        "README.md"
    ]

    missing_files = []
    for filename in required_files:
        if os.path.exists(filename):
            print(f"✓ {filename}")
        else:
            print(f"❌ {filename} (missing)")
            missing_files.append(filename)

    if missing_files:
        print(f"\n⚠️  {len(missing_files)} demo files are missing")
        print("Make sure you have all demo files in the current directory")
        return False

    print("✓ All demo files are present")
    return True


def run_quick_test():
    """Run a quick test to ensure everything works."""
    print("\nRunning quick test...")

    try:
        from gbl import Gbl, ParseResultSuccess

        # Create a minimal GBL file
        builder = Gbl().GblBuilder.create()
        builder.application(type_val=32, version=0x10000)
        test_data = bytearray(b"TEST")
        builder.prog(flash_start_address=0x1000, data=test_data)

        gbl_bytes = builder.build_to_byte_array()
        print(f"✓ Created test GBL file ({len(gbl_bytes)} bytes)")

        # Parse it back
        gbl = Gbl()
        result = gbl.parse_byte_array(gbl_bytes)

        if isinstance(result, ParseResultSuccess):
            tags = result.result_list
            print(f"✓ Parsed {len(tags)} tags successfully")

            # Check for expected tags
            tag_names = [tag.tag_type.name for tag in tags if tag.tag_type]
            print(f"✓ Found tags: {', '.join(tag_names)}")

            return True
        else:
            print("❌ Parsing failed")
            return False

    except Exception as e:
        print(f"❌ Quick test failed: {e}")
        return False


def show_next_steps():
    """Show user what to do next."""
    print("\n" + "=" * 50)
    print("🎉 Setup Complete!")
    print("=" * 50)

    print("\nNext steps:")
    print("1. Run individual examples:")
    print("   python create_gbl_examples.py")
    print("   python parse_gbl_examples.py")
    print("")
    print("2. Run all examples at once:")
    print("   python run_all_examples.py")
    print("")
    print("3. Read the documentation:")
    print("   cat README.md")
    print("")
    print("4. Explore the API in your own scripts:")
    print("   from gbl import Gbl")
    print("   builder = Gbl().GblBuilder.create()")

    print("\nHappy coding with GBL files! 🚀")


def main():
    """Main setup function."""
    print("GBL Demo Setup")
    print("=" * 30)

    success_count = 0
    total_steps = 5

    # Check Python version
    if check_python_version():
        success_count += 1
    else:
        print("\nSetup failed - incompatible Python version")
        sys.exit(1)

    # Install package
    if install_package():
        success_count += 1
    else:
        print("\nSetup failed - could not install package")
        print("You may need to:")
        print("- Check your internet connection")
        print("- Update pip: python -m pip install --upgrade pip")
        print("- Install manually: pip install gbl-ninja-python")
        sys.exit(1)

    # Verify installation
    if verify_installation():
        success_count += 1
    else:
        print("\nSetup failed - package verification failed")
        sys.exit(1)

    # Check demo files
    if check_demo_files():
        success_count += 1
    else:
        print("\nSome demo files are missing, but core functionality should work")

    # Run quick test
    if run_quick_test():
        success_count += 1
    else:
        print("\nQuick test failed - there may be an issue with the installation")
        sys.exit(1)

    # Show results
    print(f"\n✅ Setup completed successfully ({success_count}/{total_steps} steps)")

    if success_count == total_steps:
        show_next_steps()
    else:
        print("\n⚠️  Setup completed with some issues")
        print("Core functionality should work, but some features may be limited")


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n\n❌ Setup interrupted by user")
        sys.exit(1)
    except Exception as e:
        print(f"\n❌ Setup failed with error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)