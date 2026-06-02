#!/usr/bin/env python3
import os
import sys
import argparse
import subprocess
from pathlib import Path

# Project root directory
PROJECT_ROOT = Path(__file__).parent.parent.parent.parent.parent

def check_quality():
    """Check code quality and provide improvement recommendations"""
    print("Checking code quality...")
    
    # Check ESLint
    print("\nRunning ESLint...")
    try:
        result = subprocess.run(
            ['pnpm', 'lint:eslint'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ ESLint passed")
        else:
            print("✗ ESLint failed")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"⚠ ESLint not available: {e}")
    
    # Check Prettier
    print("\nRunning Prettier...")
    try:
        result = subprocess.run(
            ['pnpm', 'lint:prettier'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ Prettier passed")
        else:
            print("✗ Prettier failed")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"⚠ Prettier not available: {e}")
    
    # Check Stylelint
    print("\nRunning Stylelint...")
    try:
        result = subprocess.run(
            ['pnpm', 'lint:stylelint'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ Stylelint passed")
        else:
            print("✗ Stylelint failed")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"⚠ Stylelint not available: {e}")
    
    # Check TypeScript
    print("\nRunning TypeScript check...")
    try:
        result = subprocess.run(
            ['pnpm', 'type:check'],
            cwd=PROJECT_ROOT,
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            print("✓ TypeScript check passed")
        else:
            print("✗ TypeScript check failed")
            print(result.stdout)
            print(result.stderr)
    except Exception as e:
        print(f"⚠ TypeScript check not available: {e}")
    
    # Provide improvement recommendations
    print("\nImprovement Recommendations:")
    print("1. Fix any ESLint errors")
    print("2. Run Prettier to format code")
    print("3. Fix any Stylelint errors")
    print("4. Resolve TypeScript type issues")
    print("5. Consider adding unit tests")

def main():
    parser = argparse.ArgumentParser(description='Check code quality and provide improvement recommendations')
    subparsers = parser.add_subparsers(dest='command', help='Command to execute')
    
    # Quality command
    quality_parser = subparsers.add_parser('quality', help='Check code quality')
    
    args = parser.parse_args()
    
    if args.command == 'quality':
        check_quality()
    else:
        parser.print_help()

if __name__ == '__main__':
    main()
